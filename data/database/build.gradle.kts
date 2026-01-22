plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room.schema)
    id("kmp.koin.ksp")
    id("kmp.room.ksp")
}

room {
    schemaDirectory("$projectDir/schema")
}

ksp {
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
    arg("room.schmaLocation", "$projectDir/schema")
}

kotlin {
    androidLibrary {
        namespace = "eu.caraus.kmp.database"
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
        withDeviceTest {
            enableCoverage = true
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            execution = "TEST"
            androidResources {
                enable = true
            }
        }
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
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation(libs.room.ktx)
        }
        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.koin.core.viewmodel)
                implementation(libs.room.testing)
                implementation(libs.androidx.test.compose.manifest)
                implementation(libs.androidx.test.compose.junit)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.test.espresso)
            }
        }
    }
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}
