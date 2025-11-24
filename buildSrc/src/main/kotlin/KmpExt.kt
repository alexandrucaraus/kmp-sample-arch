package eu.caraus.kmp.build

import eu.caraus.kmp.build.host.HostOS
import org.gradle.api.Plugin
import org.gradle.api.Project
//import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
//
//class MyKmpPlugin : Plugin<Project> {
//    override fun apply(project: Project) {
//        project.plugins.withId("org.jetbrains.kotlin.multiplatform") {
//            val kmp = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
//
//            // Access all KMP targets
//            kmp.targets.all { target ->
//                println("KMP target: ${target.name}")
//            }
//        }
//    }
//}

/**
 * Check if running on Mac (for conditional logic in build scripts)
 */
fun Project.isMac(): Boolean = HostOS.isMac()

/**
 * Check if running on Linux
 */
fun Project.isLinux(): Boolean = HostOS.isLinux()

/**
 * Check if running on Windows
 */
fun Project.isWindows(): Boolean = HostOS.isWindows()

/**
 * Get current host OS
 */
fun Project.hostOS(): HostOS = HostOS.current()