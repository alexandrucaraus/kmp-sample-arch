#!/bin/sh

#
# Runs unit and instrumented tests on android
#

cd ..

./scripts/emulator-manager.sh start

./gradlew clean

./gradlew testAndroid connectedAndroidTest

./gradlew featuresCoverageReport

./scripts/emulator-manager.sh stop


# debug
##./scripts/emulator.sh start
#
#./gradlew clean
#
#clear
#
#./gradlew :feature:notes:tests:testAndroidHostTest
#./gradlew :feature:notes:tests:androidConnectedCheck
#
##./gradlew :feature:notes:ui:featureModuleCoverageReport
#./gradlew :feature:notes:tests:featureCoverageReport
##./gradlew :feature:notes:data:androidModulesCoverageReport
##./gradlew :feature:notes:domain:androidModulesCoverageReport
#
##./scripts/emulator.sh stop
