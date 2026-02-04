#!/bin/sh

#
# Runs unit and instrumented tests on android
#

cd ..

./scripts/emulator-manager.sh start

./gradlew clean

./gradlew testAndroid connectedAndroidTest

./gradlew featuresCoverageReport totalCoverageReport

./scripts/emulator-manager.sh stop
