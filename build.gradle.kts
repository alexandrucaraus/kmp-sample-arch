import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.modulegraph) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.multiplatform.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.kover) apply true
    alias(libs.plugins.ktlint)
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

buildscript {
    dependencies {
        classpath("io.nlopez.compose.rules:ktlint:0.5.3")
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.8.0")
        //android.set(true)
        //verbose.set(true)
        outputToConsole.set(true)
        coloredOutput.set(true)

//        reporters {
//            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
//            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
//        }

        if (!HostManager.hostIsMac) {
            tasks.configureEach {
                if (name.contains("compileKotlinIos", ignoreCase = true)) {
                    enabled = false
                }
            }
        }

        filter {
//            exclude("**/generated/**")
//            exclude("**/build/**")
            exclude { it.file.path.contains("/generated/") }

            if (!HostManager.hostIsMac) {
                exclude("**/iosMain/**")
                exclude("**/iosTest/**")
                exclude("**/iosX64Main/**")
                exclude("**/iosArm64Main/**")
                exclude("**/iosSimulatorArm64Main/**")
            }
        }

        //enableExperimentalRules.set(true)


//        dependencies {
//            ktlintRuleset("io.nlopez.compose.rules:ktlint:0.5.3")
//        }

    }


}

dependencies {
    ktlintRuleset(libs.ktlint.compose.rules)
}

// Design system
// https://atomicdesign.bradfrost.com/chapter-2/
