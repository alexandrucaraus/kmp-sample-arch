plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.modulegraph) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.multiplatform.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.ktlint)
    id("kmp.jacoco") apply true
    id("kmp.linter") apply true
}

jacoco {
    toolVersion = "0.8.14"
}

// Design system
// https://atomicdesign.bradfrost.com/chapter-2/
