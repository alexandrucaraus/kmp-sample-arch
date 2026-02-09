#!/bin/sh

#
# Runs unit and instrumented tests on android
#

cd ..

./scripts/emulator-manager.sh start

./gradlew clean

./gradlew testAndroid connectedAndroidTest

./gradlew androidFeatureCoverageReport

./scripts/emulator-manager.sh stop
