#!/bin/sh

#
# Runs unit and instrumented tests on android
#

cd ..
./gradlew testAndroid connectedAndroidTest
