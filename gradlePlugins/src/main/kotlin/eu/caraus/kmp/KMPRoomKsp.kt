package eu.caraus.kmp

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.konan.target.HostManager

class KMPRoomKsp : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("com.google.devtools.ksp")
        project.dependencies {
            val libs = project.extensions.getByType(
                VersionCatalogsExtension::class.java
            ).named("libs")
            val koinCompiler = libs.findLibrary("room-compiler").get()
            add("kspCommonMainMetadata", koinCompiler)
            add("kspAndroid", koinCompiler)
            if (HostManager.hostIsMac) {
                add("ksp", koinCompiler)
//                add("kspIosArm64", koinCompiler)
//                add("kspIosSimulatorArm64", koinCompiler)
            }
        }
    }
}
