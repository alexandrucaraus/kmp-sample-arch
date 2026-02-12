package eu.caraus.kmp.coverage

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class KmpTotalCoverageReport : Plugin<Project> {

    private val rootCoverageTaskName = "kmpTotalCoverageReport"

    val excludedProjects = setOf(
        "test-common",
        "tests"
    )

    override fun apply(project: Project) {
        require(project == project.rootProject) {
            "Must apply to root project"
        }
        project.plugins.apply("org.jetbrains.kotlinx.kover")
        project.dependencies {
            project.subprojects.toList()
                .filter {
                    it.buildFile.exists()
                }
                .forEach {
                    "kover"(project(it.path))
                }
        }
        project.extensions.configure<KoverProjectExtension>("kover") {
            currentProject {
                createVariant("custom") {}
            }
        }
        applySubProjectsPlugin(project)
        configureRootTask(project)
    }

    private fun configureRootTask(project: Project) {
        project.tasks.register(rootCoverageTaskName) {
            group = "Reporting"
            description = "Kover total coverage report"
            dependsOn("koverHtmlReport", "koverXmlReport")
        }
    }

    private fun applySubProjectsPlugin(project: Project) {
        if (!project.buildFile.exists()) return
        project.subprojects.filterNot { it.path in excludedProjects }.forEach { sub ->
            with(sub) {
                pluginManager.withPlugin("com.android.application") {
                    apply(plugin = "org.jetbrains.kotlinx.kover")
                    configure<KoverProjectExtension> {
                        currentProject {
                            createVariant("custom") {
                                add("debug")
                            }
                        }
                    }
                }
                pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
                    pluginManager.withPlugin("com.android.kotlin.multiplatform.library") {
                        apply(plugin = "org.jetbrains.kotlinx.kover")
                        configure<KoverProjectExtension> {
                            currentProject {
                                createVariant("custom") {
                                    add("android")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
