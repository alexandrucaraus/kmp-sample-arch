package eu.caraus.kmp.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * Generates total project android test coverage
 * depends on connectedAndroidTests running on emulator
 * and android unit tests
 */
class AndroidTotalCoverageReport : Plugin<Project> {
    private val rootCoverageTaskName = "androidTotalCoverageReport"

    val excludedProjects = ExcludedModulesFromCoverage.excluded

    override fun apply(project: Project) {
        require(project == project.rootProject) {
            "Must apply to root project"
        }
        project.afterEvaluate {
            registerRootCoverageTask(project)
        }
    }

    private fun registerRootCoverageTask(project: Project) {
        project.plugins.apply("jacoco")
        project.tasks.register<JacocoReport>(rootCoverageTaskName) {

            group = "Reporting"
            description = "Total coverage report android unit and instrumented coverage report"

            mustRunAfter(
                project.subprojects
                    .exclude(excludedProjects)
                    .flatMap { subproject ->
                        subproject.tasks.filter { task ->
                            task.name.startsWith("compile") ||
                                task.name.startsWith("process") ||
                                task.name.startsWith("generate") ||
                                task.name.startsWith("package") ||
                                task.name.startsWith("merge") ||
                                task.name.startsWith("check")
                        }
                    }
            )

            sourceDirectories.setFrom(
                project.provider {
                    project.files(
                        project.subprojects
                            .exclude(excludedProjects)
                            .map { subproject ->
                                subproject.files(
                                    coverageSources
                                        .map { path -> subproject.file(path) }
                                        .filter { it.exists() }
                                        .map { it.path }
                                )
                            }
                    )
                }
            )

            classDirectories.setFrom(
                project.provider {
                    project.files(
                        project.subprojects
                            .exclude(excludedProjects)
                            .map { sub ->
                                sub.fileTree(sub.layout.buildDirectory) {
                                    include(*coverageClasses)
                                    exclude(*coverageExcludedClasses)
                                }.filter { !it.hasComposablePreview() }
                            }
                    )
                }
            )

            executionData.setFrom(
                project.provider {
                    project.subprojects
                        .exclude(excludedProjects)
                        .flatMap { subproject ->
                            subproject.fileTree(subproject.layout.buildDirectory) {
                                include(*coverageOutput)
                            }.files
                        }
                }
            )

            reports {
                xml.required.set(true)
                html.required.set(true)
            }
        }
    }

    private fun Set<Project>.exclude(modules: Set<String>) =
        filterNot { it.name in modules }


    val coverageSources = arrayOf(
        "src/commonMain/kotlin",
        "src/androidMain/kotlin",
        "src/main/kotlin",
        "src/main/java",
    )

    val coverageClasses = arrayOf(
        "**/classes/kotlin/**"
    )

    val coverageExcludedClasses = arrayOf(
        "**/ksp/generated/**",
        "**/test/common/**/*.*",
        "**/tests/**/*.*"
    )

    val coverageOutput = arrayOf(
        "**/outputs/unit_test_code_coverage/**/*.exec",
        "**/outputs/code_coverage/**/*.ec",
    )
}
