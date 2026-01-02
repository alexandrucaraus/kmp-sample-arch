import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    //alias(libs.plugins.room.schema)
    id("kmp.compilation.host")
    id("kmp.room.ksp")
    id("kmp.koin.ksp")
}

ksp {
    arg("KOIN_CONFIG_CHECK", "false")
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
//    arg("room.schemaLocation", "$rootDir/data/database/schema")
//    arg("room.incremental", "true")
//    arg("room.expandProjection", "true")
}

//room {
//    schemaDirectory("$rootDir/data/database/schema")
//}

kotlin {
    androidTarget()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {

            implementation(projects.features.notes.domain)
            //implementation(projects.data.database)

            implementation(libs.room.runtime)

//            implementation(compose.runtime)
//            implementation(compose.foundation)
//            implementation(compose.material3)
//            implementation(compose.materialIconsExtended)
//            implementation(compose.components.resources)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
//            implementation(libs.compose.navigation)
//            implementation(libs.compose.navigation.common)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
//            implementation(libs.koin.compose.viewmodel)
//            implementation(libs.koin.core.viewmodel)
            implementation(libs.koin.annotations)

           // implementation(libs.sqldelight.coroutine.ext)

        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "eu.caraus.kmp.notes.data"
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
