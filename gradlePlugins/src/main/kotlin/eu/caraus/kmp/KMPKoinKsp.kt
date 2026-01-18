package eu.caraus.kmp

import com.google.devtools.ksp.gradle.KspExtension
import eu.caraus.kmp.host.isMac
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension

open class KmpKoinKspExtension {
    var useKoinViewModel = true
    var checkConfig = false
}

class KMPKoinKsp : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {

            val extension = extensions.create(
                "kmpKoinKsp",
                KmpKoinKspExtension::class.java
            )

           plugins.apply("com.google.devtools.ksp")
           plugins.withId("org.jetbrains.kotlin.multiplatform") {
               addKspDependencies(project, extension)
            }
        }
    }

    private fun addKspDependencies(project: Project, extension: KmpKoinKspExtension) {
        val koinCompiler = project
            .extensions
            .getByType(VersionCatalogsExtension::class.java)
            .named("libs")
            .findLibrary("koin-compiler")
            .orElseThrow { Exception("Koin compiler not found") }
        project.dependencies.apply {
            add("kspCommonMainMetadata", koinCompiler)
            add("kspAndroid", koinCompiler)
            if (project.isMac()) {
                add("kspIosArm64", koinCompiler)
                add("kspIosSimulatorArm64", koinCompiler)
            }
        }
        val kspExtension = project.extensions.getByType(KspExtension::class.java)
        kspExtension.apply {
            if (!extension.useKoinViewModel) {
                arg("KOIN_USE_COMPOSE_VIEWMODEL", "false")
            }
            arg("KOIN_CONFIG_CHECK", extension.checkConfig.toString())
        }
    }

//    private fun configureKspSourceSets(project: Project) {
//        // Get Kotlin Multiplatform extension
//        val kotlin = project.extensions.findByType(
//            org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension::class.java
//        ) ?: return
//
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
//    }
}
