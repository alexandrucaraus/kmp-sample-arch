#!/bin/sh

#
# Runs coverage on ci
#

./gradlew clean && \
./gradlew verifyPaparazzi
./gradlew testAndroid && \
./gradlew connectedAndroidTest && \
./gradlew androidFeatureCoverageReport && \
./gradlew androidTotalCoverageReport && \
./gradlew kmpTotalCoverageReport
