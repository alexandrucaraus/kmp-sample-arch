import eu.caraus.kmp.coverage.AndroidEmulatorTask

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.multiplatform.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.paparazzi) apply false
    id("kmp.jacoco") apply true
    id("kmp.linter") apply true
    alias(libs.plugins.modulegraph) apply true
    id("kmp.feature.skeleton") apply false
}

// Design system
// https://atomicdesign.bradfrost.com/chapter-2/

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "### Modules Structure"
    rootModulesRegex.set(":app")
    excludedModulesRegex.set(".*test.*")
    nestingEnabled = true
    showFullPath = true
    includeIsolatedModules = true
    setStyleByModuleType = true
}

tasks.register("starLocaltTestEmulator", AndroidEmulatorTask::class) {
    avdName.set("instrumented_test_emulator")
    systemImage.set("system-images;android-33;google_apis;x86_64")
    deviceType.set("pixel_5")
    bootTimeout.set(3000)
    emulatorAction.set("start")
}

tasks.register("startLocalTestEmulator", AndroidEmulatorTask::class) {
    emulatorAction.set("stop")
}

tasks.register<Delete>("clean") {
    group = "build"
    description = "Deletes the build directory and all submodule build directories"

    // Delete root build directory
    delete(rootProject.layout.buildDirectory)

    // Delete all subproject build directories
    rootProject.subprojects {
        delete(layout.buildDirectory)
        delete(layout.projectDirectory.file(".gradle"))
    }
}
