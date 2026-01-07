package eu.caraus.kmp

import eu.caraus.kmp.host.isMac
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies

class KMPKoinKsp : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("com.google.devtools.ksp")
        project.dependencies {
            // Access version catalog
            val libs = project.extensions.getByType(
                VersionCatalogsExtension::class.java
            ).named("libs")
            val koinCompiler = libs.findLibrary("koin-compiler").get()
            // Add KSP dependencies for different targets
            if (project.isMac()) {
                add("kspCommonMainMetadata", koinCompiler)
                add("kspAndroid", koinCompiler)
                add("ksp", koinCompiler)
            } else {
                add("kspCommonMainMetadata", koinCompiler)
                add("kspAndroid", koinCompiler)
            }
        }
    }
}
