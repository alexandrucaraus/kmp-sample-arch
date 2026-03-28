#!/bin/sh

#
# Runs unit and instrumented tests on android
#

is_project_root="$(stat ./gradlew 2>/dev/null)"
while [ ! "$is_project_root" ]; do
    cd ..
    is_project_root="$(stat ./gradlew 2>/dev/null)"
done

./gradlew ktlintCheck detekt

./scripts/emulator.sh start

./gradlew clean

./gradlew testAndroid verifyPaparazzi connectedAndroidTest

./gradlew androidFeatureCoverageReport androidTotalCoverageReport kmpTotalCoverageReport

./scripts/emulator.sh stop
