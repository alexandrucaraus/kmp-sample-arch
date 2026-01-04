package eu.caraus.kmp

import eu.caraus.kmp.host.isMac
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KMPCompilationHostOS : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("org.jetbrains.kotlin.multiplatform")
        val kmp = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        if (project.isMac()) {
            with(kmp) {
                androidTarget()
                iosArm64()
                iosSimulatorArm64()
            }
        } else {
            with(kmp) {
                androidTarget()
            }
        }
    }
}
