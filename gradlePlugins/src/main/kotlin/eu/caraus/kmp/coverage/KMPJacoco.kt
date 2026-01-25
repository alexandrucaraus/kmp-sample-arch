package eu.caraus.kmp.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport

class KMPJacoco : Plugin<Project> {

    override fun apply(project: Project) {
        require(project == project.rootProject) {
            "KMPJacoco must be applied to the root project only"
        }

        // Apply JaCoCo to root
        project.plugins.apply("jacoco")

        // Apply JaCoCo to all subprojects

        project.subprojects.forEach { subproject ->
            subproject.plugins.apply("jacoco")

            // JVM / Android unit tests
//            tasks.withType<Test>().configureEach {
//                extensions.configure(JacocoTaskExtension::class.java) {
//                    isIncludeNoLocationClasses = true
//                    excludes = listOf("jdk.internal.*")
//                }
//            }

            // Android & KMP Android plugins
//            pluginManager.withPlugin("com.android.library") {
//                enableAndroidCoverage(this)
//            }


            subproject.pluginManager.withPlugin("com.android.application") {
                enableAndroidCoverage(subproject)
            }

            subproject.pluginManager.withPlugin("com.android.kotlin.multiplatform.library") {
                enableAndroidCoverage(subproject)
            }
        }

        registerAggregationTask(project)
    }

    private fun enableAndroidCoverage(project: Project) {
        project.tasks.matching {
            it.name.contains("AndroidTest", ignoreCase = true)
        }.configureEach {
//            extensions.configure(JacocoTaskExtension::class.java) {
//                isIncludeNoLocationClasses = true
//            }
        }
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

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            executionData.from(
                project.subprojects.map { sub ->
                    sub.fileTree(sub.layout.buildDirectory) {
                        include(
                            "**/unit_test_code_coverage/**/*.exec",
                            "**/code_coverage/**/*.ec"
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
        }
    }
}

//class KMPJacoco : Plugin<Project> {
//
//    override fun apply(project: Project) {
//        aggregated(project)
//    }
//
//    fun aggregated(project: Project) {
//        if (project != project.rootProject) {
//            throw GradleException("KMPJacocoAggregation must be applied to root project only")
//        }
//
//        project.plugins.apply("jacoco")
//
//        with(project) {
//                project.tasks.register<JacocoReport>("jacocoAndroidTestReport") {
//                    group = "Reporting"
//                    description = "Generate aggregated Jacoco coverage report from all modules for Android unit and instrumented tests"
//
//                    // Depends on running emulator
//                    //dependsOn("startTestEmulator")
//
////                    doLast {
////                        // todo kills all the emulators needs only to kill the one
////                        // started for coverage
////                        ProcessBuilder("adb", "emu", "kill").start().waitFor()
////                    }
//
////                    dependsOn(
////                        project.tasks.findByName("createAndroidDeviceTestCoverageReport")
////                    )
//
//                    // Depend on all module test tasks
//                    dependsOn(subprojects.flatMap { subproject ->
//                        listOfNotNull(
//                            subproject.tasks.findByName("testDebugUnitTest"),
//                            subproject.tasks.findByName("connectedDebugAndroidTest"),
//                            subproject.tasks.findByName("testAndroidHostTest"),
//                            subproject.tasks.findByName("connectedAndroidDeviceTest")
//                        )
//                    })
//
//                    reports {
//                        xml.required.set(true)
//                        html.required.set(true)
//                    }
//
//                    val fileFilter = setOf(
//                        "**/*Preview*.*",
//                        "org/koin/ksp/generated/**/*.*",
//                        "eu/caraus/kmp/test/common/**/*.*"
//                    )
//
//                    // Aggregate sources from all modules
//                    val allSourceDirs = subprojects.flatMap { subproject ->
//                        listOfNotNull(
//                            subproject.file("src/commonMain/kotlin").takeIf { it.exists() },
//                            subproject.file("src/androidMain/kotlin").takeIf { it.exists() },
//                            subproject.file("src/main/kotlin").takeIf { it.exists() },
//                            subproject.file("src/main/java").takeIf { it.exists() }
//                        )
//                    }
//                    sourceDirectories.setFrom(project.files(allSourceDirs))
//
//                    // Aggregate classes from all modules
//                    val allClassDirs = subprojects.flatMap { subproject ->
//                        val buildDir = subproject.layout.buildDirectory.get().asFile
//
//                        listOfNotNull(
//                            // KMP Android classes
//                            subproject.fileTree("$buildDir/classes/kotlin/android/main") {
//                                exclude(fileFilter)
//                            }.takeIf { it.dir.exists() },
//
//                            // Standard Android classes
//                            subproject.fileTree("$buildDir/tmp/kotlin-classes/debug") {
//                                exclude(fileFilter)
//                            }.takeIf { it.dir.exists() },
//                        )
//                    }
//                    classDirectories.setFrom(project.files(allClassDirs))
//
//                    val allExecutionData = subprojects.flatMap { subproject ->
//                        val buildDir = subproject.layout.buildDirectory.get().asFile
//
//                        subproject.fileTree(buildDir).matching {
//                            include(
//                                // KMP Android unit tests
//                                "outputs/unit_test_code_coverage/androidHostTest/*.exec",
//                                // KMP Android instrumented tests
//                                "outputs/code_coverage/androidDeviceTest/**/*.ec",
//                            )
//                        }.files.filter { it.exists() }
//                    }
//
//                    executionData.setFrom(project.files(allExecutionData))
//
//                    doLast {
//                        reports.xml.outputLocation.orNull?.asFile?.let {
//                            logger.lifecycle("📊 JaCoCo aggregated XML report: ${it.absolutePath}")
//                        }
//
//                        reports.html.outputLocation.orNull?.asFile?.let {
//                            logger.lifecycle("📊 JaCoCo aggregated HTML report: ${it.absolutePath}")
//                        }
//                    }
//            }
//
//        }
//
////        project
////            .tasks
////            .findByName("jacocoAndroidTestReport")
////            ?.dependsOn(":app:createAndroidDeviceTestCoverageReport")
//    }
//}
