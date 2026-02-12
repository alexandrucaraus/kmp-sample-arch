package eu.caraus.kmp.coverage

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

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
        project.extensions.configure<KoverProjectExtension>("kover") {
            reports {
                total {
                    xml.onCheck.set(true)
                    html.onCheck.set(true)
                }
            }
        }

        configureModulesTask(project)

        project.afterEvaluate {
            configureTask(project)
        }
    }

    private fun configureModulesTask(project: Project) {
//        project.tasks.register(rootCoverageTaskName) {
//
//            group = "Coverage"
//            description = "Kover total coverage report"

            project.subprojects.forEach { module ->
                //if (module.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                    module.plugins.apply("org.jetbrains.kotlinx.kover")
                    module.extensions.configure<KoverProjectExtension>("kover") {
                        reports {
                            total {
                                xml.onCheck.set(true)
                                html.onCheck.set(true)
                            }
                        }
                    }
               // }
            }
       // }
    }

    private fun configureTask(project: Project) {
        project.tasks.register(rootCoverageTaskName) {

            group = "Coverage"
            description = "Kover total coverage report"

            dependsOn(
                project.tasks.filter { it.name in listOf("koverHtmlReport", "koverXmlReport") },
                project.tasks.filter { it.name in listOf("koverHtmlReport", "koverXmlReport") }
            )
        }
    }
}
