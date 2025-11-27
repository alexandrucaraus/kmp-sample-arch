import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room.schema)
    id("kmp.compilation.host")
    id("kmp.koin.ksp")
    id("kmp.room.ksp")
//    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {

            api(projects.features.notes)

            api(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            implementation(libs.kotlinx.coroutines.core)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)

            implementation(libs.sqldelight.coroutine.ext)

        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.sqldelight.driver)
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

//sqldelight {
//    databases {
//        create("Database") {
//            packageName.set("eu.caraus.kmp.database.sql")
//        }
//    }
//}

android {
    namespace = "eu.caraus.kmp.database"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        lint.targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

room {
    schemaDirectory("$projectDir/schema")
}

ksp {
    arg("KOIN_CONFIG_CHECK", "false")
}
