plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.modulegraph) apply false
}

android {
    namespace = "eu.caraus.kmp.samplearch.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "eu.caraus.kmp.samplearch.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(projects.features.notes)
    implementation(projects.data.database)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    debugImplementation(libs.compose.ui.tooling)
}

//moduleGraphConfig {
//    readmePath.set("./../README.md")
//    heading = "### Module Graph"
//    theme.set(
//        Theme.BASE(
//            mapOf(
//                "primaryTextColor" to "#fff",
//                "primaryColor" to "#5a4f7c",
//                "primaryBorderColor" to "#5a4f7c",
//                "lineColor" to "#f5a623",
//                "tertiaryColor" to "#40375c",
//                "fontSize" to "12px",
//            ),
//            focusColor = "#FA8140"
//        ),
//    )
//}