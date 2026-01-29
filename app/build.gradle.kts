

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinx.serialization)
    id("kmp.koin.ksp")
}

kmpKoinKsp {
    checkConfig = true
}

kotlin {
    applyDefaultHierarchyTemplate()
    val appId = "eu.caraus.kmp.samplearch"
    androidLibrary {
        namespace = appId
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
        withHostTest {
            enableCoverage = true
            isIncludeAndroidResources = true
        }
        withDeviceTest {
            enableCoverage = true
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            androidResources {
                enable = true
            }
        }
        testCoverage {}
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "composeApp"
            freeCompilerArgs += "-Xbinary=bundleId=$appId"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.features.notes.domain)
                implementation(projects.features.notes.data)
                implementation(projects.features.notes.ui)
                implementation(projects.data.database)

                implementation(libs.compose.material3)
                implementation(libs.compose.nav3)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.nav3)
                implementation(libs.koin.annotations)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(projects.testCommon)
                implementation(libs.koin.test)
                implementation(libs.koin.core.viewmodel)
                implementation(libs.androidx.test.compose.manifest)
                implementation(libs.androidx.test.compose.junit)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.test.espresso)
            }
        }
    }
}
