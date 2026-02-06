import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinx.serialization)
    id("kmp.koin.ksp")
}

kmpKoinKsp {
    useKoinViewModel = true
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = "eu.caraus.kmp.notes.tests"
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
        withHostTest {
            enableCoverage = true
            isIncludeAndroidResources = true
        }
        withDeviceTest {
            enableCoverage = true
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        androidResources {
            enable = true
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        testCoverage {
        }
    }
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonTest.dependencies {
            implementation(projects.testCommon)
            implementation(projects.features.notes.domain)
            implementation(projects.features.notes.ui)
            implementation(projects.features.notes.data)
            implementation(projects.data.database)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.material3.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.nav3)
            implementation(libs.koin.core)
            implementation(libs.koin.test)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(projects.testCommon)
                api(projects.features.notes.domain)
                api(projects.features.notes.ui)
                api(projects.features.notes.data)
                api(projects.data.database)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin.core)
                implementation(libs.koin.annotations)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.compose.material3.icons.extended)
                implementation(libs.compose.material3)
                implementation(libs.compose.nav3)

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

dependencies {
    add("androidHostTestImplementation", libs.paparazzi.classgraph)
}
