import dev.iurysouza.modulegraph.Theme

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.modulegraph)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

android {
    namespace = "eu.caraus.kmp.samplearch.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "eu.caraus.kmp.samplearch.android"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
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
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

dependencies {
    implementation(projects.app)

    implementation(libs.compose.material3)

    implementation(libs.androidx.activity.compose)

    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.koin.android)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit)

    implementation(libs.compose.ui.tooling)
}

moduleGraphConfig {
    readmePath.set("./../README.md")
    heading = "### Modules Structure"
    showFullPath = true
    setStyleByModuleType = true
    rootModulesRegex.set(":androidApp")
    includeIsolatedModules = true
    nestingEnabled = true
    theme.set(
        Theme.BASE(
            mapOf(
                "primaryTextColor" to "#fff",
                "primaryColor" to "#5a4f7c",
                "primaryBorderColor" to "#5a4f7c",
                "lineColor" to "#f5a623",
                "tertiaryColor" to "#40375c",
                "fontSize" to "12px",
            ),
            focusColor = "#FA8140"
        ),
    )
}

//jacoco {
//    toolVersion = "0.8.14"
//}

ktlint {
}
