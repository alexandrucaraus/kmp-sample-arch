package eu.caraus.kmp.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

class KMPJacoco : Plugin<Project> {

    override fun apply(project: Project) {
        require(project == project.rootProject) {
            "KMPJacoco must be applied to the root project only"
        }
        project.plugins.apply("jacoco")
        project.subprojects.forEach { subproject ->
            subproject.plugins.apply("jacoco")
        }
        registerAggregationTask(project)
    }


    private fun registerAggregationTask(project: Project) {
        project.tasks.register<JacocoReport>("androidTestCoverage") {
            group = "Reporting"
            description = "Aggregated JaCoCo coverage for all KMP Android modules"

            dependsOn(
                project.subprojects.flatMap { sub ->
                    sub.tasks.matching {
                        it.name in setOf(
                            "testDebugUnitTest",
                            "connectedDebugAndroidTest",
                            "testAndroidHostTest",
                            "connectedAndroidDeviceTest"
                        )
                    }
                }
            )

            sourceDirectories.setFrom(
                project.files(
                    project.subprojects.flatMap { sub ->
                        listOf(
                            sub.file("src/commonMain/kotlin"),
                            sub.file("src/androidMain/kotlin"),
                            sub.file("src/main/kotlin"),
                            sub.file("src/main/java")
                        ).filter { it.exists() }
                    }
                )
            )

            classDirectories.setFrom(
                project.files(
                    project.subprojects.flatMap { sub ->
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
            )

            executionData.from(
                project.provider {
                    project.subprojects.flatMap { subproject ->
                        subproject.fileTree(subproject.layout.buildDirectory) {
                            include(
                                "**/unit_test_code_coverage/**/*.exec",
                                "**/code_coverage/**/*.ec",
                                "**/outputs/unit_test_code_coverage/**/*.exec",
                                "**/outputs/code_coverage/**/*.ec",
                                "**/jacoco/testDebugUnitTest.exec",
                                "**/coverage.ec"
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
}
