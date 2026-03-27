#!/bin/sh

#
# Runs coverage on ci
#

export JAVA_HOME=/usr/bin

./gradlew clean && \
./gradlew testAndroid && \
./gradlew connectedAndroidTest && \
./gradlew androidFeatureCoverageReport && \
./gradlew androidTotalCoverageReport && \
./gradlew kmpTotalCoverageReport
