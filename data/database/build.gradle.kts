import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room.schema)
    id("kmp.koin.ksp")
    id("kmp.room.ksp")
}

kotlin {
    androidLibrary {
        namespace = "eu.caraus.kmp.database"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        lint.targetSdk = libs.versions.android.targetSdk.get().toInt()
        withJava()
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(projects.features.notes.data)

            api(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.koin.core)
            implementation(libs.koin.annotations)

            implementation(libs.sqldelight.coroutine.ext)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

room {
    schemaDirectory("$projectDir/schema")
}

ksp {
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}
