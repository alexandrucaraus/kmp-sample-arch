package eu.caraus.kmp.coverage

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.org.bouncycastle.oer.its.ieee1609dot2.EndEntityType.app
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

class KMPJacoco : Plugin<Project> {

    override fun apply(project: Project) {
        aggregated(project)
    }

    fun aggregated(project: Project) {
        if (project != project.rootProject) {
            throw GradleException("KMPJacocoAggregation must be applied to root project only")
        }

        project.plugins.apply("jacoco")

        with(project) {
                project.tasks.register<JacocoReport>("jacocoAndroidTestReport") {
                    group = "Reporting"
                    description = "Generate aggregated Jacoco coverage report from all modules for Android unit and instrumented tests"

                    // Depends on running emulator
                    //dependsOn("startTestEmulator")

//                    doLast {
//                        // todo kills all the emulators needs only to kill the one
//                        // started for coverage
//                        ProcessBuilder("adb", "emu", "kill").start().waitFor()
//                    }

                    dependsOn(
                        project.tasks.findByName("createAndroidDeviceTestCoverageReport")
                    )

                    // Depend on all module test tasks
                    dependsOn(subprojects.flatMap { subproject ->
                        listOfNotNull(
                            subproject.tasks.findByName("testDebugUnitTest"),
                            subproject.tasks.findByName("connectedDebugAndroidTest"),
                            subproject.tasks.findByName("testAndroidHostTest"),
                            subproject.tasks.findByName("connectedAndroidDeviceTest")
                        )
                    })

                    reports {
                        xml.required.set(true)
                        html.required.set(true)
                    }

                    val fileFilter = setOf(
                        "**/*Preview*.*",
                        "org/koin/ksp/generated/**/*.*",
                        "eu/caraus/kmp/test/common/**/*.*"
                    )

                    // Aggregate sources from all modules
                    val allSourceDirs = subprojects.flatMap { subproject ->
                        listOfNotNull(
                            subproject.file("src/commonMain/kotlin").takeIf { it.exists() },
                            subproject.file("src/androidMain/kotlin").takeIf { it.exists() },
                            subproject.file("src/main/kotlin").takeIf { it.exists() },
                            subproject.file("src/main/java").takeIf { it.exists() }
                        )
                    }
                    sourceDirectories.setFrom(project.files(allSourceDirs))

                    // Aggregate classes from all modules
                    val allClassDirs = subprojects.flatMap { subproject ->
                        val buildDir = subproject.layout.buildDirectory.get().asFile

                        listOfNotNull(
                            // KMP Android classes
                            subproject.fileTree("$buildDir/classes/kotlin/android/main") {
                                exclude(fileFilter)
                            }.takeIf { it.dir.exists() },

                            // Standard Android classes
                            subproject.fileTree("$buildDir/tmp/kotlin-classes/debug") {
                                exclude(fileFilter)
                            }.takeIf { it.dir.exists() },
                        )
                    }
                    classDirectories.setFrom(project.files(allClassDirs))

                    val allExecutionData = subprojects.flatMap { subproject ->
                        val buildDir = subproject.layout.buildDirectory.get().asFile

                        subproject.fileTree(buildDir).matching {
                            include(
                                // KMP Android unit tests
                                "outputs/unit_test_code_coverage/androidHostTest/*.exec",
                                // KMP Android instrumented tests
                                "outputs/code_coverage/androidDeviceTest/**/*.ec",
                            )
                        }.files.filter { it.exists() }
                    }

                    executionData.setFrom(project.files(allExecutionData))

                    doLast {
                        reports.xml.outputLocation.orNull?.asFile?.let {
                            logger.lifecycle("📊 JaCoCo aggregated XML report: ${it.absolutePath}")
                        }

                        reports.html.outputLocation.orNull?.asFile?.let {
                            logger.lifecycle("📊 JaCoCo aggregated HTML report: ${it.absolutePath}")
                        }
                    }
            }

        }

        project
            .tasks
            .findByName("jacocoAndroidTestReport")
            ?.dependsOn(":app:createAndroidDeviceTestCoverageReport")
    }
}
