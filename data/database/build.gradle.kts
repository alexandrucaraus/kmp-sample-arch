import com.android.tools.r8.internal.fa

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.kotlinx.serialization)
    // alias(libs.plugins.room)
    alias(libs.plugins.koin.compiler)
    // id("com.google.devtools.ksp")
    // alias(libs.plugins.ksp)
    id("kmp.room.ksp")
}

koinCompiler {
    compileSafety = false // Enabled by default
    skipDefaultValues = true // Enabled by default
}

kotlin {
    android {
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
            androidResources {
                enable = true
            }
        }
        androidResources {
            enable = true
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
            // implementation(libs.room.ktx)
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

// room3 {
//    schemaDirectory("${project.rootDir}/data/database/schema")
// }
//
// ksp {
//    arg("room.incremental", "true")
//    arg("room.expandProjection", "true")
// }
//
// dependencies {
//    ksp(libs.room.compiler)
//    add("kspCommonMainMetadata", libs.room.compiler)
//    add("kspAndroid", libs.room.compiler)
//    add("kspIosArm64", libs.room.compiler)
//    add("kspIosSimulatorArm64", libs.room.compiler)
// }
