package eu.caraus.kmp

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import kotlin.jvm.java
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File

class KMPFeatureSkeleton : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register(
            "createFeatureModule",
            CreateFeatureTask::class.java
        )
    }
}

abstract class CreateFeatureTask : DefaultTask() {

    @Option(
        option = "featureName",
        description = "Name of the feature (module)"
    )
    @Input
    var featureName: String? = null

    @Option(
        option = "packageName",
        description = "kotlin package name"
    )
    @Input
    var packageName: String? = null

    @Option(
        option = "layer",
        description = "domain, ui, data, test or all (all previous)",
    )
    @Input
    var layer: String = "all"

    @TaskAction
    fun create() {

        val featureName = featureName
            ?: throw GradleException("Requires feature name e.g: --featureName=notes")

        val packageName = packageName
            ?: throw GradleException("Requires package name e.g: --packageName=org.stuff.io")

        val layer = layer.also {
            if (it !in listOf("domain", "ui", "data", "test", "all")) {
                throw GradleException("layer should be one of: domain, ui, data, test, all")
            }
        }

        val featureDir = File("./", "features/$featureName").also {
            if (!it.exists()) it.mkdirs()
        }

        createDomainModule(featureDir = featureDir)

        when (layer) {
            "ui" -> {
                createUiModule(
                    featureDir = featureDir,
                )
            }

            "data" -> {
                createDataModule(
                    featureDir = featureDir,
                )
            }

            "all" -> {
                createUiModule(
                    featureDir = featureDir,
                )
                createDataModule(
                    featureDir = featureDir,
                )
            }
        }
    }

    private fun createDomainModule(
        featureDir: File,
        layer: String = "domain",
    ) {
        val domainModuleDir = File(featureDir, layer).also {
            if (!it.exists()) it.mkdirs()
        }

        File(domainModuleDir, "build.gradle.kts").writeText(
            """
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.kotlinx.serialization)
    id("kmp.koin.ksp")
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = $packageName.$layer
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        lint.targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        withJava()
    }
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
    }
}

            """.trimIndent()
        )

        val packageNamePath =
            packageName.plus(".$layer").replace(".", "/")

        listOf(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin"
        ).forEach { sourceSet ->
            File(domainModuleDir, "$sourceSet/$packageNamePath").makedirs()
        }
    }

    private fun createUiModule(
        featureDir: File,
        layer: String = "ui",
    ) {
        val uiModuleDir = File(featureDir, layer).also {
            if (!it.exists()) it.mkdirs()
        }

        File(uiModuleDir, "build.gradle.kts").writeText(
            """
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinx.serialization)
    id("kmp.koin.ksp")
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = $packageName.$layer
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        lint.targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        withJava()
        testCoverage {}
    }
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonMain.dependencies {
            // Common deps

            // Feature deps
            implementation(projects.features.$featureName.domain)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.material3.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.nav3)
            implementation(libs.compose.ui.tooling.preview)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
        }
    }
}
            """.trimIndent()
        )

        val packageNamePath = packageName.plus(".$layer").replace(".", "/")

        listOf(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin"
        ).forEach { sourceSet ->
            File(uiModuleDir, "$sourceSet/$packageNamePath").makedirs()
        }
    }

    private fun createDataModule(
        featureDir: File, layer: String = "data",
    ) {
        val dataModuleDir = File(featureDir, layer).also {
            if (!it.exists()) it.mkdirs()
        }

        File(dataModuleDir, "build.gradle.kts").writeText(
            """
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room.schema)
    id("kmp.room.ksp")
    id("kmp.koin.ksp")
}

ksp {
    arg("room.schemaLocation", "${'$'}rootDir/data/database/schema")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

room {
    schemaDirectory("${'$'}rootDir/data/database/schema")
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = $packageName.$layer
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        lint.targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        withJava()
    }
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonMain.dependencies {
            // Common deps

            // Feature deps
            implementation(projects.features.$featureName.domain)

            implementation(libs.room.runtime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
    }
}
            """.trimIndent()
        )

        val packageNamePath = packageName.plus(".$layer").replace(".", "/")

        listOf(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin"
        ).forEach { sourceSet ->
            File(dataModuleDir, "$sourceSet/$packageNamePath").makedirs()
        }

    }

    fun File.makedirs() {
        fun mkdirs(
            file: File,
            orig: File,
        ) {
            if (orig.exists()) return

            if (!file.parentFile.exists()) {
                mkdirs(file.parentFile, orig)
            } else {
                file.mkdir()
                mkdirs(orig, orig)
            }
        }
        mkdirs(this, this)
    }

}



