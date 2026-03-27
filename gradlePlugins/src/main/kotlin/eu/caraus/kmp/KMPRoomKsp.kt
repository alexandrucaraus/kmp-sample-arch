package eu.caraus.kmp

import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.internal.cc.base.logger
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KMPRoomKsp : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("com.google.devtools.ksp")
        project.plugins.apply("androidx.room")

        project.extensions.configure<RoomExtension> {
            logger.lifecycle("Schema location ./data/database/schema")
            schemaDirectory(
                "./data/database/schema"
            )
        }

        project.extensions.configure<KspExtension> {
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }

        project.afterEvaluate {
            project.configurations.forEach { config ->
                if (config.name.contains("ksp", ignoreCase = true)) {
                    logger.lifecycle("KSP config: ${config.name}")
                }
            }
        }

        project.afterEvaluate {
            project.dependencies {
                val libs = project.extensions
                    .getByType(VersionCatalogsExtension::class.java)
                    .named("libs")
                val roomCompiler = libs.findLibrary("room-compiler").get()
                add("kspCommonMainMetadata", roomCompiler)
                add("kspAndroid", roomCompiler)
            }
        }
    }
}
