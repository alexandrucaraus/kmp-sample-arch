plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room.schema)
    id("kmp.room.ksp")
    id("kmp.koin.ksp")
}

kotlin {
    androidLibrary {
        namespace = "eu.caraus.kmp.notes.data"
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
            implementation(projects.features.notes.domain)

            implementation(libs.room.runtime)

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

ksp {
    arg("room.schemaLocation", "$rootDir/data/database/schema")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

room {
    schemaDirectory("$rootDir/data/database/schema")
}
