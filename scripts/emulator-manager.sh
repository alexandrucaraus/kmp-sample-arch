#!/bin/bash
# scripts/emulator-manager.sh
# Shared emulator management for both pre-push hooks and CI

set -e

# Prevent recursive Git hook execution
export GIT_DIR=""
export GIT_WORK_TREE=""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Default configuration
DEFAULT_EMULATOR_NAME="local_instrumentation_emulator"
DEFAULT_TIMEOUT=120
DEFAULT_API_LEVEL=33

# Usage function
usage() {
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  start     Start the emulator"
    echo "  stop      Stop the emulator"
    echo "  status    Check emulator status"
    echo "  setup     Setup emulator (create AVD if needed)"
    echo ""
    echo "Options:"
    echo "  --name NAME       Emulator name (default: $DEFAULT_EMULATOR_NAME)"
    echo "  --timeout SECS    Boot timeout in seconds (default: $DEFAULT_TIMEOUT)"
    echo "  --api-level NUM   Android API level (default: $DEFAULT_API_LEVEL)"
    echo "  --help           Show this help message"
    echo ""
    echo "Environment Variables:"
    echo "  ANDROID_HOME     Android SDK path (default: ~/.android/sdk)"
    echo "  EMULATOR_PID     Process ID of running emulator"
}

# Parse command line arguments
COMMAND=""
EMULATOR_NAME="$DEFAULT_EMULATOR_NAME"
TIMEOUT="$DEFAULT_TIMEOUT"
API_LEVEL="$DEFAULT_API_LEVEL"

while [[ $# -gt 0 ]]; do
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

# Set ANDROID_HOME if not set
if [ -z "$ANDROID_HOME" ]; then
    possible_paths=(
            "$HOME/.android/sdk"
            "$HOME/Library/Android/sdk"
            "$HOME/Android/Sdk"
            "/usr/local/android-sdk"
        )

        for path in "${possible_paths[@]}"; do
            if [ -d "$path" ]; then
                export ANDROID_HOME="$path"
                echo -e "${YELLOW}ANDROID_HOME not set, using found path: $ANDROID_HOME${NC}"
                break
            fi
        done

        if [ -z "$ANDROID_HOME" ]; then
            echo -e "${YELLOW}ANDROID_HOME not set and no known SDK path found.${NC}"
        fi
fi

# Validate Android SDK
if [ ! -d "$ANDROID_HOME" ]; then
    echo -e "${RED}Android SDK not found at: $ANDROID_HOME${NC}"
    exit 1
fi

# Find cmdline-tools
find_cmdline_tools() {
    local tools_path=""

    if [ -d "$ANDROID_HOME/cmdline-tools/latest" ] && [ -f "$ANDROID_HOME/cmdline-tools/latest/bin/avdmanager" ]; then
        tools_path="$ANDROID_HOME/cmdline-tools/latest"
    else
        # Find highest version
        local highest_version=""
        for version_dir in "$ANDROID_HOME/cmdline-tools"/*; do
            if [ -d "$version_dir" ] && [ -f "$version_dir/bin/avdmanager" ]; then
                local version=$(basename "$version_dir")
                if echo "$version" | grep -q '^[0-9]'; then
                    if [ -z "$highest_version" ]; then
                        highest_version="$version"
                        tools_path="$version_dir"
                    else
                        if [ "$(printf '%s\n%s\n' "$highest_version" "$version" | sort -V | tail -n1)" = "$version" ]; then
                            highest_version="$version"
                            tools_path="$version_dir"
                        fi
                    fi
                fi
            fi
        done
    fi

    if [ -z "$tools_path" ]; then
        echo -e "${RED}Android command-line tools not found${NC}"
        exit 1
    fi

    echo "$tools_path"
}

# Detect architecture and get system image
get_system_image() {
    local api_level="$1"

    if [ "$(uname -m)" = "arm64" ]; then
        echo "system-images;android-${api_level};google_apis;arm64-v8a"
    else
        echo "system-images;android-${api_level};google_apis;x86_64"
    fi
}

# Stop emulator
stop_emulator() {
    echo -e "${YELLOW}Stopping emulator...${NC}"

    EMULATOR_PID=$(pgrep -f local_instrumentation_emulator)

    # Kill process if PID is available
    if [ ! -z "$EMULATOR_PID" ]; then
        kill "$EMULATOR_PID" 2>/dev/null 1>/dev/null
    fi

    echo -e "${GREEN}Emulator stopped${NC}"
}

# Setup AVD if needed
setup_avd() {
    local tools_path="$(find_cmdline_tools)"
    local system_image="$(get_system_image "$API_LEVEL")"

    echo -e "${YELLOW}Setting up AVD: $EMULATOR_NAME${NC}"

    # Check if AVD exists
    local avd_path="$HOME/.android/avd/${EMULATOR_NAME}.avd"
    if [ -d "$avd_path" ]; then
        echo -e "${GREEN}AVD '$EMULATOR_NAME' already exists${NC}"
        return 0
    fi

    echo -e "${YELLOW}Creating AVD with system image: $system_image${NC}"

    # Accept licenses
    echo "y" | "$tools_path/bin/sdkmanager" --licenses >/dev/null 2>&1

    # Install system image if needed
    if ! "$tools_path/bin/sdkmanager" --list_installed 2>/dev/null | grep -q "$system_image"; then
        echo -e "${YELLOW}Installing system image: $system_image${NC}"
        echo "y" | "$tools_path/bin/sdkmanager" "$system_image"

        if [ $? -ne 0 ]; then
            echo -e "${RED}Failed to install system image, trying fallback...${NC}"
            local fallback_api=$((API_LEVEL - 1))
            local fallback_image="$(get_system_image "$fallback_api")"
            echo "y" | "$tools_path/bin/sdkmanager" "$fallback_image"
            if [ $? -eq 0 ]; then
                system_image="$fallback_image"
                echo -e "${GREEN}Using fallback image: $system_image${NC}"
            else
                echo -e "${RED}Failed to install any system image${NC}"
                return 1
            fi
        fi
    fi

    # Create AVD
    echo "no" | "$tools_path/bin/avdmanager" create avd \
        --force \
        --name "$EMULATOR_NAME" \
        --package "$system_image" \
        --device "pixel_3a" 2>/dev/null

    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Successfully created AVD: $EMULATOR_NAME${NC}"
        return 0
    else
        echo -e "${RED}Failed to create AVD${NC}"
        return 1
    fi
}

# Start emulator
start_emulator() {
    if check_emulator_status; then
        echo -e "${GREEN}Emulator is already running${NC}"
        return 0
    fi

    # Setup AVD if needed
    setup_avd

    echo -e "${YELLOW}Starting emulator: $EMULATOR_NAME${NC}"

    # Start emulator in background
    "$ANDROID_HOME/emulator/emulator" -avd "$EMULATOR_NAME" \
        -no-audio \
        -no-window \
        -no-snapshot-save \
        -no-snapshot-load \
        -wipe-data \
        -gpu swiftshader_indirect \
        -memory 4048 \
        -cores 4 &

    local emulator_pid=$!
    echo "EMULATOR_PID=$emulator_pid"

    # Export PID for external use
    if [ ! -z "$GITHUB_ENV" ]; then
        echo "EMULATOR_PID=$emulator_pid" >> "$GITHUB_ENV"
    fi

    echo -e "${YELLOW}Waiting for emulator to boot (timeout: ${TIMEOUT}s)...${NC}"

    # Wait for emulator to be ready
    local timeout=0
    while [ $timeout -lt $TIMEOUT ]; do
        if adb shell getprop sys.boot_completed 2>/dev/null | grep -q "1"; then
            echo -e "${GREEN}Emulator is ready!${NC}"
            sleep 3  # Extra wait for stability
            adb devices
            return 0
        fi

        sleep 3
        timeout=$((timeout + 3))
        echo -e "${YELLOW}Still waiting... (${timeout}s/${TIMEOUT}s)${NC}"
    done

    echo -e "${RED}Emulator failed to start within ${TIMEOUT} seconds${NC}"
    return 1
}

# Main command execution
case "$COMMAND" in
    start)
        start_emulator
        ;;
    stop)
        stop_emulator
        ;;
    setup)
        setup_avd
        ;;
    *)
        echo "Unknown command: $COMMAND"
        usage
        exit 1
        ;;
esac
