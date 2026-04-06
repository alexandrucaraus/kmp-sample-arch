# Kotlin Multiplatform Sample Arch
Modular Kotlin Multiplatform Application

## Project structure

```
.
├── androidApp
├── app
│   ├── build.gradle.kts
│   └── src
├── build
│   ├── kover
│   └── reports
├── config
│   └── detekt
├── data
│   └── database
features/
└── notes
    ├── data
    ├── domain
    ├── tests
    └── ui
├── gradle
│   ├── libs.versions.toml
├── gradlePlugins
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src
├── gradle.properties
├── iosApp
│   ├── iosApp
│   └── iosApp.xcodeproj
├── scripts
│   ├── create-feature-skeleton.sh
│   ├── emulator.sh
│   ├── push-no-verify.sh
│   ├── test-android-coverage-ci.sh
│   ├── test-android-local.sh
│   ├── test-android-pre-push.sh
│   └── test-lint.sh
├── settings.gradle.kts
└── test-common
    ├── build
    ├── build.gradle.kts
    ├── consumer-rules.pro
    ├── proguard-rules.pro
    └── src
```

## Structure description

**androidApp**, **iosApp** - consumer modules, app entry point, loads app library.<br>
**app** - umbrella module for the whole app, aggregates all other modules.<br>
**features** - a subdirectory that holds features of the app, each feature has a name which is a root folder,
and domain, data, ui and test modules. All the tests should be kept in test module for the feature.<br>
**data** - module, holds infrastructure modules configuration, databases, generic network configurations, etc...<br>
**test-common** - contains common utilities used by feature test modules.<br>
library versions are managed through gradle/libs.versions.toml<br>
**gradlePlugins** - custom gradle plugins, that make it easier to apply them across modules.<br>
**scripts** - handy scripts to run various stuff.

## Linters
Project uses ktlint and detekt
```shell
./gradlew ktlintCheck detekt
```

Some formatting can be fixed with

```shell
./gradle ktlintFormat
```

## Testing

### Unit Testing

Runs commonTest for android variant
```shell
./gradlew testAndroid
```

### Emulator Testing
Runs android device test androidDeviceTest variant (executes on Emulator)
```shell
./gradlew connectedAndroidTest
```

Can be run outside of IDE like following (ex. with coverage report)
```shell
./scripts/emulator.sh start

./gradlew clean

./gradlew connectedAndroidTest

./gradlew androidFeatureCoverageReport androidTotalCoverageReport kmpTotalCoverageReport

./scripts/emulator.sh stop
```

### Snapshot testing (cash app paparazzi)
Test are in androidHostTest variant (executes on the dev machine JVM, no emulator required)
```shell
# Verifies previously generated screenshots
./gradlew verifyPaparazzi
```

```shell
# Generates screenshots
./gradlew recordPaparazzi

# Generate screenshots for a module
./gradle :features:notes:tests:recordPaparazzi
```

## Test coverage

### Android Test Coverage (JACOCO)
Generates coverage report per feature and total per app.
Covers the test results from jvm run as well as emulator run, combines them into one.
```shell
./gradlew androidFeatureCoverageReport androidTotalCoverageReport
```

### Common Test Coverage (KOVER)
Generates coverage report per feature and total per app, covers only results of the common code
(no plaform/emulator code covered.)
```shell
./gradlew kmpTotalCoverageReport
```
