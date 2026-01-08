package eu.caraus.kmp

import eu.caraus.kmp.host.isMac
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies

class KMPKoinKsp : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("com.google.devtools.ksp")

        project.afterEvaluate {
            configureKspSourceSets(project)
            addKspDependencies(project)
        }
    }

    private fun configureKspSourceSets(project: Project) {
        // Get Kotlin Multiplatform extension
        val kotlin = project.extensions.findByType(
            org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension::class.java
        ) ?: return

//        kotlin.sourceSets.apply {
//            // Configure commonMain
//            named("commonMain").configure {
//                kotlin {
//                    srcDir("build/generated/ksp/metadata/commonMain/kotlin")
//                }
//            }
//
//            // Configure commonTest (if it exists)
//            named("commonTest").configure {
//                kotlin {
//                    srcDir("build/generated/ksp/metadata/commonTest/kotlin")
//                }
//            }
//        }
    }

    private fun addKspDependencies(project: Project) {
        val libs = project.extensions.getByType(
            VersionCatalogsExtension::class.java
        ).named("libs")
        val koinCompiler = libs.findLibrary("koin-compiler").get()

        project.dependencies {
   //         add("kspCommonMainMetadata", koinCompiler)
            add("kspAndroid", koinCompiler)
            add("ksp", koinCompiler)

//            if (project.isMac()) {
//                add("ks", koinCompiler)
//                add("kspAndroidTest", koinCompiler)
//            }
        }
    }
}
