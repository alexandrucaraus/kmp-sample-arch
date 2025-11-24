import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room.schema)
//    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {}
//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {

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

//    sourceSets.named("commonMain").configure {
//        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
//    }
//    sourceSets.named("androidMain").configure {
//        kotlin.srcDir("build/generated/ksp/android/androidDebug/kotlin")
//    }

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

dependencies {
   // add("kspCommonMainMetadata", libs.room.compiler)
    add("kspAndroid", libs.room.compiler)
    //add("kspIosX64", libs.room.compiler)
//    add("kspIosArm64", libs.room.compiler)
//    add("kspIosSimulatorArm64", libs.room.compiler)
}

dependencies {
   // add("kspCommonMainMetadata", libs.koin.compiler)
    add("kspAndroid", libs.koin.compiler)
//    add("kspIosX64", libs.koin.compiler)
//    add("kspIosArm64",libs.koin.compiler)
//    add("kspIosSimulatorArm64", libs.koin.compiler)
}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    dependsOn("kspCommonMainDebugKotlinMetadata") // For commonMain
//    dependsOn("kspDebugKotlinAndroid")      // For androidDebug variant
//    // Add other KSP tasks if you have more build variants or targets
//    // e.g., dependsOn("kspReleaseKotlinAndroid")
//}

room {
    schemaDirectory("$projectDir/schema")
}

ksp {
    arg("KOIN_CONFIG_CHECK", "false")
}
