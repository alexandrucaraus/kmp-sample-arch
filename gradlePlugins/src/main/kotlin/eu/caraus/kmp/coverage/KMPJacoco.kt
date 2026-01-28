package eu.caraus.kmp.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

class KMPJacoco : Plugin<Project> {
    private val rootCoverageTaskName = "androidCoverageReport"

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
            description = "Root Android unit and instrumented coverage report"

            val excludedProjects = setOf(
                "test-common",
            )

            sourceDirectories.setFrom(
                project.provider {
                    project.files(
                        project.subprojects
                            .exclude(excludedProjects)
                            .flatMap { sub ->
                                listOf(
                                    sub.file("src/commonMain/kotlin"),
                                    sub.file("src/androidMain/kotlin"),
                                    sub.file("src/main/kotlin"),
                                    sub.file("src/main/java")
                                ).filter { it.exists() }
                            }
                    )
                }
            )

            classDirectories.setFrom(
                project.provider {
                    project.files(
                        project.subprojects
                            .exclude(excludedProjects)
                            .flatMap { sub ->
                                sub.fileTree(sub.layout.buildDirectory) {
                                    include(
                                        "**/kotlin-classes/**",
                                        "**/classes/kotlin/**"
                                    )
                                    exclude(
                                        "**/R.class",
                                        "**/R$*.class",
                                        "**/BuildConfig.*",
                                        "**/*Test*.*"
                                    )
                                }
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
                                include(
                                    "**/unit_test_code_coverage/**/*.exec",
                                    "**/code_coverage/**/*.ec",
                                    "**/outputs/unit_test_code_coverage/**/*.exec",
                                    "**/outputs/code_coverage/**/*.ec",
                                    "**/jacoco/testDebugUnitTest.exec",
                                    "**/coverage.ec"
                                )
                            }.files.also {
                                it.forEach { file ->
                                    logger.lifecycle("${file.absolutePath}")
                                }
                            }
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
}
