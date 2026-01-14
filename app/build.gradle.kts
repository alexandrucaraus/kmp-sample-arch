

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

    val appId = "eu.caraus.kmp.samplearch"

    androidLibrary {
        namespace = appId
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        lint.targetSdk = libs.versions.android.targetSdk.get().toInt()
        withJava()
        withHostTest { }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
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
                implementation(libs.koin.annotations)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.compiler)
    add("kspAndroid", libs.koin.compiler)
    add("ksp", libs.koin.compiler)
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
}