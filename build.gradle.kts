import org.jetbrains.kotlin.konan.target.HostManager

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
}

jacoco {
    toolVersion = "0.8.14"
}

// Todo put below behind a ktlin local plugin
buildscript {
    dependencies {
        classpath("io.nlopez.compose.rules:ktlint:0.5.3") {
            exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
        }
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.8.0")
        outputToConsole.set(true)
        coloredOutput.set(true)

        if (!HostManager.hostIsMac) {
            tasks.configureEach {
                if (name.contains("compileKotlinIos", ignoreCase = true)) {
                    enabled = false
                }
            }
        }

        filter {
            exclude { it.file.path.contains("/generated/") }

            if (!HostManager.hostIsMac) {
                exclude("**/iosMain/**")
                exclude("**/iosTest/**")
                exclude("**/iosX64Main/**")
                exclude("**/iosArm64Main/**")
                exclude("**/iosSimulatorArm64Main/**")
            }
        }
    }
}

dependencies {
    ktlintRuleset(libs.ktlint.compose.rules)
}

// Design system
// https://atomicdesign.bradfrost.com/chapter-2/
