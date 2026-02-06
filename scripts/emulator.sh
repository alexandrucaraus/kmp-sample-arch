#!/bin/sh

set -e

# Default configuration
COMMAND=""
DEFAULT_EMULATOR_NAME="local_instrumentation_emulator"
DEFAULT_TIMEOUT=120
DEFAULT_API_LEVEL=33

# Parse command line arguments
EMULATOR_NAME="$DEFAULT_EMULATOR_NAME"
TIMEOUT="$DEFAULT_TIMEOUT"
API_LEVEL="$DEFAULT_API_LEVEL"

main () {

# Parse command line arguments
while [ $# -gt 0 ]; do
    case $1 in
        start|stop|status|setup)
            COMMAND="$1"
            shift
            ;;
        --name)
            EMULATOR_NAME="$2"
            shift 2
            ;;
        --timeout)
            TIMEOUT="$2"
            shift 2
            ;;
        --api-level)
            API_LEVEL="$2"
            shift 2
            ;;
        --help|-h)
            usage
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            usage
            exit 1
            ;;
    esac
done

if [ -z "$COMMAND" ]; then
    echo "Error: No command specified"
    usage
    exit 1
fi

# Main command execution
case "$COMMAND" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    *)
        echo "Unknown command: $COMMAND"
        usage
        exit 1
        ;;
esac
}

# Usage function
usage() {
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  start     Start the emulator"
    echo "  stop      Stop the emulator"
    echo ""
    echo "Options:"
    echo "  --name NAME       Emulator name (default: $DEFAULT_EMULATOR_NAME)"
    echo "  --timeout SECS    Boot timeout in seconds (default: $DEFAULT_TIMEOUT)"
    echo "  --api-level NUM   Android API level (default: $DEFAULT_API_LEVEL)"
    echo "  --help           Show this help message"
    echo ""
}

start() {

    if is_emulator_running; then
        stop_emulator
    fi

    init_sdk_info

    if ! is_emulator_set_up; then
        setup_emulator
    fi

    if ! is_emulator_running; then
        start_emulator
    fi

    exit 0
}

stop() {
    stop_emulator
}

is_emulator_running() {
    EMULATOR_PID="$(pidof "$EMULATOR_NAME")"
    if [ -n "$EMULATOR_PID" ]; then
        log_success "Emulator $EMULATOR_NAME already running"
        return 0
    else
        log_info "Emulator $EMULATOR_NAME is not running"
        return 1
    fi
}

init_sdk_info() {
    log_info "Setting up sdk paths..."

    # Setup ANDROID_HOME var
    if [ -z "$ANDROID_HOME" ]; then
        for sdk_path in \
            "$HOME/.android/sdk" \
            "$HOME/Library/Android/sdk" \
            "$HOME/Android/Sdk" \
            "/usr/local/android-sdk"
        do
            if [ -d "$sdk_path" ]; then
                ANDROID_HOME="$sdk_path"
                export ANDROID_HOME
                break
            fi
        done
        if [ -z "$ANDROID_HOME" ]; then
            log_error "ANDROID_HOME not set and no known SDK path found."
            exit 1
        fi
    fi

    # Setup ANDROID_SDK_TOOLS
    if [ -d "$ANDROID_HOME/cmdline-tools/latest" ]; then
        export ANDROID_SDK_TOOLS="$ANDROID_HOME/cmdline-tools/latest"
    else
        cmdline_tools_dir="$ANDROID_HOME/cmdline-tools"
        highest_version_path=$(
            ls "$cmdline_tools_dir/" 2>/dev/null \
                | grep -Eo '[0-9]+(\.[0-9]+)*' \
                | sort -V \
                | tail -n 1
        )
        export ANDROID_SDK_TOOLS="$cmdline_tools_dir/$highest_version_path"
    fi

    export SDK_MANAGER_CMD="$ANDROID_SDK_TOOLS/bin/sdkmanager"
    export AVD_MANAGER_CMD="$ANDROID_SDK_TOOLS/bin/avdmanager"
    export EMULATOR_CMD="$ANDROID_HOME/emulator/emulator"

    log_info "ANDROID_HOME=<$ANDROID_HOME>"
    log_info "ANDROID_SDK_TOOLS=<$ANDROID_SDK_TOOLS>"
    log_info "SDK_MANAGER_CMD=<$SDK_MANAGER_CMD>"
    log_info "AVD_MANAGER_CMD=<$AVD_MANAGER_CMD>"
    log_info "EMULATOR_CMD=<$EMULATOR_CMD>"
}

is_emulator_set_up() {
    emulator_path="$HOME/.android/avd/$EMULATOR_NAME.avd"
    if [ -d "$emulator_path" ]; then
        log_success "AVD $EMULATOR_NAME already exists"
        return 0
    else
        log_info "AVD $EMULATOR_NAME not found"
        return 1
    fi
}

get_system_image() {
    api_level="$1"
    if [ "$(uname -m)" = "arm64" ]; then
        echo "system-images;android-${api_level};google_apis;arm64-v8a"
    else
        echo "system-images;android-${api_level};google_apis;x86_64"
    fi
}

setup_emulator() {
    log_info "Setting up $EMULATOR_NAME"
    system_image=get_system_image "$API_LEVEL"
    log_info "Creating AVD $EMULATOR_NAME with system_image $system_image"
    # Accept licenses
    echo "y" | "$SDK_MANAGER_CMD" --licenses >/dev/null 2>&1
    # Install system image if needed
    if ! "$SDK_MANAGER_CMD" --list_installed 2>/dev/null | grep -q "$system_image"; then
        log_info "Installing system image $system_image"
        echo "y" | "$SDK_MANAGER_CMD $system_image"
        if [ $? -ne 0 ]; then
            log_error "Failed to install system image, trying fallback..."
            fallback_api=$((API_LEVEL - 1))
            fallback_image="$(get_system_image "$fallback_api")"
            echo "y" | "$SDK_MANAGER_CMD" "$fallback_image"
            if [ $? -eq 0 ]; then
                system_image="$fallback_image"
                log_success "Using fallback image $system_image"
            else
                log_error "Failed to install any system image"
                exit 1
            fi
        fi
    fi
    # Create AVD
    echo "no" | "AVD_MANAGER_CMD" create avd \
        --force \
        --name "$EMULATOR_NAME" \
        --package "$system_image" \
        --device "pixel_3a" 2>/dev/null
    if [ $? -eq 0 ]; then
        log_success "Successfully created AVD: $EMULATOR_NAME"
        return 1
    else
        log_error "Failed to create AVD"
        exit 1
    fi
}

start_emulator() {
    log_info "Starting $EMULATOR_NAME"
    ADB_COMMAND="$ANDROID_HOME/platform-tools/adb"

    existing_emulators=$($ADB_COMMAND devices | grep "emulator-" | awk '{print $1}' 2>/dev/null)

    # Start emulator in background
    "$EMULATOR_CMD" -avd "$EMULATOR_NAME" \
        -no-audio \
        -no-window \
        -no-snapshot-save \
        -no-snapshot-load \
        -wipe-data \
        -gpu swiftshader_indirect \
        -memory 4048 \
        -cores 4 > /dev/null 2>&1 &
    EMULATOR_PID=$!

    # Wait for a new emulator to appear
    timeout=0
    NEW_EMULATOR_SERIAL=""
    while [ $timeout -lt "$TIMEOUT" ]; do
        # Get current list of emulators
        current_emulators=$("$ADB_COMMAND" devices | grep "emulator-" | awk '{print $1}')

        # Find the new emulator (not in the original list)
        for emu in $current_emulators; do
            if ! echo "$existing_emulators" | grep -q "^$emu$"; then
                export NEW_EMULATOR_SERIAL="$emu"
                echo "$NEW_EMULATOR_SERIAL" > /tmp/EMULATOR_SERIAL
                break 2  # Break both loops
            fi
        done

        sleep 3
        timeout=$((timeout + 3))
        log_info "Waiting for emulator to appear... (${timeout}s/${TIMEOUT}s)"
        done

        if [ -z "$NEW_EMULATOR_SERIAL" ]; then
            log_error "No new emulator detected within ${TIMEOUT} seconds"
            kill $EMULATOR_PID 2>/dev/null
            exit 1
        fi

        log_info "Found new emulator: $NEW_EMULATOR_SERIAL"
        log_info "Waiting for boot completion..."

        # Now wait for THIS specific emulator to boot
        timeout=0
        while [ $timeout -lt "$TIMEOUT" ]; do
            if "$ADB_COMMAND" -s "$NEW_EMULATOR_SERIAL" shell getprop sys.boot_completed 2>/dev/null | grep -q "1"; then
                log_success "Emulator $NEW_EMULATOR_SERIAL is ready!"
                sleep 3
                adb devices
                export EMULATOR_SERIAL="$NEW_EMULATOR_SERIAL"  # Export for later use
                return 0
            fi
            sleep 3
            timeout=$((timeout + 3))
            log_info "Booting $NEW_EMULATOR_SERIAL... (${timeout}s/${TIMEOUT}s)"
        done

        log_error "Emulator $NEW_EMULATOR_SERIAL failed to boot within ${TIMEOUT} seconds"
        kill $EMULATOR_PID 2>/dev/null
        exit 1
}

stop_emulator() {
    log_info "Stopping emulator name <$EMULATOR_NAME>"
    EMULATOR_PID="$(pidof "$EMULATOR_NAME")"
    if [ -n "$EMULATOR_PID" ]; then
        log_info "Emulator $EMULATOR_NAME pid $EMULATOR_PID found"
        kill "$EMULATOR_PID">/dev/null 2>&1
        rm -f /tmp/EMULATOR_SERIAL
    else
        log_warning "Emulator $EMULATOR_NAME is not running"
    fi
    log_success "Emulator $EMULATOR_NAME stopped"
    exit 0
}

pidof() {
    PIDOF_NAME=$1
    # shellcheck disable=SC2009
    ps e| grep "$PIDOF_NAME" | grep -v grep | awk '{print $1}'
}

log_info() {
    MSG="$1"
    echo "$MSG"
}

log_success() {
    GREEN='\033[0;32m'
    NC='\033[0m'
    MSG="$1"
    printf "%b\n" "${GREEN}${MSG}${NC}"
}

log_warning() {
    YELLOW='\033[1;33m'
    NC='\033[0m'
    MSG="$1"
    printf "%b\n" "${YELLOW}${MSG}${NC}"
}

log_error() {
    RED='\033[0;31m'
    NC='\033[0m'
    MSG="$1"
    printf "%b\n" "${RED}${MSG}${NC}"
}

main "$@"
