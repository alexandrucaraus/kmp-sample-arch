package eu.caraus.kmp

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

class KMPAndroidTestCoverage : Plugin<Project> {
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
                                        // common code specific
                                        "**/*Preview*.*",
                                        "org/koin/ksp/generated/**/*.*",
                                        "eu/caraus/kmp/test/common/**/*.*",

                                        // Android specific
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
                                    // KMP Android unit tests
                                    "**/outputs/unit_test_code_coverage/**/*.exec",
                                    // KMP Android instrumented tests
                                    "**/outputs/code_coverage/**/*.ec",
                                )
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
}
