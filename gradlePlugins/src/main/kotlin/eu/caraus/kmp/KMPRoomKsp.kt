package eu.caraus.kmp

import androidx.room3.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KMPRoomKsp : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("com.google.devtools.ksp")
        project.plugins.apply("androidx.room3")

        project.extensions.configure<KotlinMultiplatformExtension> {
            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }

        project.extensions.configure<RoomExtension> {
            schemaDirectory("${project.rootDir}/data/database/schema")
        }

        project.extensions.configure<KspExtension> {
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }

        project.afterEvaluate {
            project.dependencies {
                val roomCompiler = project.extensions
                    .getByType(VersionCatalogsExtension::class.java)
                    .named("libs")
                    .findLibrary("room-compiler")
                    .get()
                add("ksp", roomCompiler)
                add("kspCommonMainMetadata", roomCompiler)
                add("kspAndroid", roomCompiler)
                add("kspIosArm64", roomCompiler)
                add("kspIosSimulatorArm64", roomCompiler)
            }
        }
    }
}
