import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    id("kmp.koin.ksp")
}

kotlin {

    applyDefaultHierarchyTemplate()

    androidLibrary {
        namespace = "eu.caraus.kmp.notes.itest"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        lint.targetSdk = libs.versions.android.targetSdk.get().toInt()
        withJava()
        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            execution = "HOST"
        }
        androidResources {
            enable = true
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {

            implementation(projects.testCommon)

            implementation(projects.features.notes.domain)
            implementation(projects.features.notes.ui)
            implementation(projects.features.notes.data)
            implementation(projects.data.database)

            implementation(libs.compose.material3.icons.extended)
            implementation(libs.compose.material3)

            implementation(libs.compose.nav3)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose.viewmodel)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)

            implementation(libs.koin.test)
            implementation(libs.koin.core.viewmodel)
        }
        val androidDeviceTest by getting {
            dependencies {
                implementation(libs.koin.test)
                implementation(libs.koin.core.viewmodel)
                implementation("androidx.compose.ui:ui-test-junit4:1.5.4")
                implementation("androidx.compose.ui:ui-test-manifest:1.5.4")
                implementation("androidx.test.ext:junit:1.1.5")
                implementation("androidx.test.espresso:espresso-core:3.5.1")
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.compiler)
    //add("kspAndroid", libs.koin.compiler)
    add("ksp", libs.koin.compiler)
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}
