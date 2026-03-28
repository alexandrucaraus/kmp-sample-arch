plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.multiplatform.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.modulegraph)
    id("app-total-android-test-coverage")
    id("app-feature-android-test-coverage")
    id("app-total-kmp-test-coverage")
    id("kmp.linter")
    id("kmp.feature.skeleton") apply false
    id("kmp.room.ksp") apply false
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

tasks.register<Delete>("clean") {
    group = "build"
    description = "Deletes the build directory and all submodule build directories"
    delete(rootProject.layout.buildDirectory)
    rootProject.subprojects {
        delete(layout.buildDirectory)
        delete(layout.projectDirectory.file(".gradle"))
    }
}
