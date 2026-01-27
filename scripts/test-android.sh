#!/bin/sh

#
# Runs unit and instrumented tests on android
#

cd ..
./gradlew startTestEmulator testAndroid connectedAndroidTest
