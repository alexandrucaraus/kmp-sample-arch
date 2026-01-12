plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    // todo should work with 2.0.0-alpha04, now it's 2.0.0-alpha02
    // https://github.com/cashapp/paparazzi/pull/2115/files
    // when 2.0.0-alpha04 is out apply it
    //alias(libs.plugins.paparazzi) apply true
    id("kmp.koin.ksp")
}

kotlin {
    androidLibrary {
        namespace = "eu.caraus.kmp.notes.ui"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        lint.targetSdk = libs.versions.android.targetSdk.get().toInt()
        withJava()
        withHostTest {}
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.notes.domain)

            implementation(libs.compose.material3.icons.extended)
            implementation(libs.compose.material3)

            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.compose.navigation)
            implementation(libs.compose.navigation.common)

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
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
        }
    }
}

dependencies {
    add("androidHostTestImplementation", libs.paparazzi.classgraph)
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.compiler)
    //add("kspAndroid", libs.koin.compiler)
    add("ksp", libs.koin.compiler)
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL", "true")
}

