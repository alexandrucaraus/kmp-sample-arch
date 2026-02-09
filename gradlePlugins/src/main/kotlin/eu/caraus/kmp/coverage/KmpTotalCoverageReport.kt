package eu.caraus.kmp.coverage

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

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

//        project.gradle.projectsEvaluated {
//            project.tasks.register("kmpTotalCoverageReport") {
//                group = "Coverage"
//                description = "Run Kover reports for all KMP modules"
//
//                // Get all kover report tasks from subprojects
//                val allKoverTasks = project.subprojects.flatMap { subproject ->
//                    subproject.tasks.filter { task ->
//                        task.name.startsWith("kover") &&
//                            task.name.endsWith("Report")
//                    }
//                }
//
//                dependsOn(allKoverTasks)
//
//                doLast {
//                    logger.lifecycle("✅ All Kover reports completed")
//                }
//            }
//        }

        project.afterEvaluate {
            configureModulesTask(project)
        }
    }

    private fun configureModulesTask(project: Project) {
        project.tasks.register(rootCoverageTaskName) {

            group = "Coverage"
            description = "Kover total coverage report"

            project.subprojects.forEach { module ->
                if (module.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                    module.plugins.apply("org.jetbrains.kotlinx.kover")
                    module.extensions.configure(KoverProjectExtension::class) {
                        reports.total.xml.onCheck.set(true)
                        reports.total.html.onCheck.set(true)
                    }
                }
            }
        }
    }
}
