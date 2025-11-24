import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {}



//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "composeApp"
//            isStatic = true
//            linkerOpts.add("-lsqlite3")
//        }
//    }

    sourceSets {


        all {
            languageSettings {
                optIn("kotlin.experimental.ExperimentalObjCName")
                // Suppress expect/actual mismatch warnings
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
                progressiveMode = false

                // MAIN SETTING:
                optIn("kotlin.ExperimentalMultiplatform")

                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }

        commonMain.dependencies {
            implementation(projects.features.notes)
            implementation(projects.data.database)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)

            implementation(libs.compose.navigation)
            implementation(libs.compose.navigation.common)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.annotations)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "eu.caraus.kmp.samplearch"
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

ksp {
    arg("KOIN_CONFIG_CHECK", "false")
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.compiler)
    add("kspAndroid", libs.koin.compiler)
//    add("kspIosX64", libs.koin.compiler)
//    add("kspIosArm64",libs.koin.compiler)
//    add("kspIosSimulatorArm64", libs.koin.compiler)
}


