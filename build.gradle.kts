import dev.iurysouza.modulegraph.Theme

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.multiplatform.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.paparazzi) apply false
    id("kmp.jacoco") apply true
    id("kmp.linter") apply true
    alias(libs.plugins.modulegraph) apply true
    id("kmp.feature.skeleton") apply true
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
