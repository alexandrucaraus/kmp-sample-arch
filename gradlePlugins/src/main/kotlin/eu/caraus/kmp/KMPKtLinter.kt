package eu.caraus.kmp

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.konan.target.HostManager
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class KMPKtLinter : Plugin<Project> {

    override fun apply(project: Project) {
        if (project != project.rootProject) {
            throw GradleException("KMPKtLinter must be applied to root project only")
        }
        ktlint(project)
        detekt(project)
    }

    private fun ktlint(project: Project) {
        project.subprojects {
            plugins.apply("org.jlleitschuh.gradle.ktlint")
            extensions.configure<KtlintExtension> {
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
    }

    private fun detekt(project: Project) {
        project.subprojects {
            plugins.apply("io.gitlab.arturbosch.detekt")
            extensions.configure<DetektExtension>() {
                toolVersion = "1.23.8"
                config.setFrom(files("${project.rootProject.projectDir}/config/detekt/detekt.yml"))
                source.from(files(
                    "src/commonMain/kotlin",
                    "src/commonTest/kotlin",
                    "src/androidMain/kotlin",
                    "src/androidTest/kotlin",
                    "src/iosMain/kotlin",
                    "src/iosTest/kotlin",
                    "src/jvmMain/kotlin",
                    "src/jvmTest/kotlin",
                    "src/jsMain/kotlin",
                    "src/jsTest/kotlin",
                ))

                dependencies {
                    add("detektPlugins","io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
                    add("detektPlugins","io.nlopez.compose.rules:detekt:0.4.23")
                }
            }
        }
    }
}
