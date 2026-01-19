package eu.caraus.kmp

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.konan.target.HostManager
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class KMPKtLinter : Plugin<Project> {

    override fun apply(project: Project) {
        if (project != project.rootProject) {
            throw GradleException("KMPKtLinter must be applied to root project only")
        }
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
}
