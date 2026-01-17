plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.modulegraph) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.multiplatform.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.kover) apply true
    alias(libs.plugins.ktlint) apply true
    id("kmp.jacoco") apply true
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

jacoco {
    toolVersion = "0.8.14"
}

ktlint {
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    plugins.withId("org.jlleitschuh.gradle.ktlint") {
        configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
            filter {
                exclude("**/build/**")
                exclude("**/generated/**")
                exclude("**/ksp/**")
            }
        }
    }
}

// Design system
// https://atomicdesign.bradfrost.com/chapter-2/
