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
    private val appCoverageTaskName = "kmpAppCoverageReport"
    private val featuresCoverageTaskName = "kmpFeaturesCoverageReport"
    private val otherCoverageTaskName = "kmpOtherCoverageReport"

    val excludedProjects = ExcludedModulesFromCoverage.excluded

    override fun apply(project: Project) {
        require(project == project.rootProject) {
            "Must apply to root project"
        }
        project.plugins.apply("org.jetbrains.kotlinx.kover")
        project.dependencies {
            project.subprojects.toList()
                .filter {
                    it.buildFile.exists() && it.name !in excludedProjects
                }
                .forEach {
                    "kover"(project(it.path))
                }
        }
        project.extensions.configure<KoverProjectExtension>("kover") {
            currentProject {
                createVariant("custom") {}
                copyVariant("app", "custom")
                copyVariant("features", "custom")
                copyVariant("other", "custom")
            }
            reports {
                variant("app") {
                    filters {
                        includes {
                            packages("eu.caraus.kmp.samplearch")
                        }
                    }
                }
                variant("features") {
                    filters {
                        includes {
                            packages(featurePackages(project))
                        }
                    }
                }
                variant("other") {
                    filters {
                        excludes {
                            packages(listOf("eu.caraus.kmp.samplearch") + featurePackages(project))
                        }
                    }
                }
            }
        }
        applySubProjectsPlugin(project)
        configureRootTask(project)
        configurePerCategoryTasks(project)
    }

    private fun featurePackages(project: Project): List<String> {
        return project.subprojects
            .filter { it.path.matches("^:features(:[^:]+){2}$".toRegex()) }
            .mapNotNull { it.path.split(":").getOrNull(2) }
            .distinct()
            .map { "eu.caraus.kmp.$it" }
    }

    private fun configureRootTask(project: Project) {
        project.tasks.register(rootCoverageTaskName) {
            group = "Reporting"
            description = "Kover total coverage report"
            dependsOn("koverHtmlReport", "koverXmlReport")
        }
    }

    private fun configurePerCategoryTasks(project: Project) {
        project.tasks.register(appCoverageTaskName) {
            group = "Reporting"
            description = "Kover app modules coverage report"
            dependsOn("koverXmlReportApp")
        }
        project.tasks.register(featuresCoverageTaskName) {
            group = "Reporting"
            description = "Kover feature modules coverage report"
            dependsOn("koverXmlReportFeatures")
        }
        project.tasks.register(otherCoverageTaskName) {
            group = "Reporting"
            description = "Kover other modules coverage report"
            dependsOn("koverXmlReportOther")
        }
    }

    private fun applySubProjectsPlugin(project: Project) {
        project.subprojects.filterNot { it.name in excludedProjects }.forEach { sub ->
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
