#!/bin/sh

#
# Runs coverage on ci
#

is_project_root="$(stat ./gradlew 2>/dev/null)"
while [ ! "$is_project_root" ]; do
    cd ..
    is_project_root="$(stat ./gradlew 2>/dev/null)"
done

./gradlew clean && \
./gradlew verifyPaparazzi && \
./gradlew testAndroid kmpTotalCoverageReport && \
./gradlew connectedAndroidTest && \
./gradlew androidFeatureCoverageReport androidTotalCoverageReport
