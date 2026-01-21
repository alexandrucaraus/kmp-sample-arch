#!/bin/sh

#
# Runs start emulator, unit and instrumented tests on android, coverage report for android
#

cd ..
./gradlew startTestEmulator jacocoAndroidTestReport
