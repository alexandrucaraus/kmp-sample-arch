plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.modulegraph) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.multiplatform.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.kover) apply true
}


dependencies {
    kover(project(":app"))
    kover(project(":features:notes:itest"))
    kover(project(":features:notes:ui"))
}

kover {
    currentProject {
        createVariant("debug") { }
    }
}

// Design system
// https://atomicdesign.bradfrost.com/chapter-2/
