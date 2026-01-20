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


//    theme.set(
//        Theme.BASE(
//            mapOf(
//                "primaryTextColor" to "#fff",
//                "primaryColor" to "#5a4f7c",
//                "primaryBorderColor" to "#5a4f7c",
//                "lineColor" to "#f5a623",
//                "tertiaryColor" to "#40375c",
//                "fontSize" to "12px",
//            ),
//            focusColor = "#FA8140",
//        ),
//    )
}
