#!/bin/sh

#
# Runs coverage on ci
#

./gradlew clean && \
./gradlew verifyPaparazzi && \
./gradlew testAndroid kmpTotalCoverageReport && \
./gradlew connectedAndroidTest && \
./gradlew androidFeatureCoverageReport androidTotalCoverageReport
