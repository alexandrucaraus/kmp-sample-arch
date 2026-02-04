#!/bin/sh

#
# Runs coverage on ci
#

./gradlew clean && \
./gradlew testAndroid && \
./gradlew connectedAndroidTest && \
./gradlew featuresCoverageReport && \
./gradlew totalCoverageReport
