#!/bin/sh

echo "Running pre-push checks..."

is_project_root="$(stat ./gradlew 2>/dev/null)"
while [ ! "$is_project_root" ]; do
    cd ..
    is_project_root="$(stat ./gradlew 2>/dev/null)"
done

# Run linters

echo "Running linters"

./gradlew ktlintCheck detekt
LINT_STATUS=$?

if [ $LINT_STATUS -ne 0 ]; then
  echo "Lint checks failed! Push aborted."
  exit 1
fi

echo "Linters OK"

# Run tests
./scripts/emulator.sh start
EMULATOR_SERIAL=$(cat /tmp/EMULATOR_SERIAL)

echo "Test emulator serial: $EMULATOR_SERIAL"

echo "Running tests"

export ANDROID_SERIAL="$EMULATOR_SERIAL"
./gradlew testAndroid connectedAndroidTest
TEST_STATUS=$?

./scripts/emulator.sh stop

if [ $TEST_STATUS -ne 0 ]; then
  echo "Tests failed! Push aborted."
  exit 1
fi

echo "Test OK"

echo "All checks passed. Proceeding with push."
exit 0
