package eu.caraus.kmp.feature.skeleton

object BuildScriptsTemplates {

    fun domain(packageName: String, layer: String = "domain"): String {
        return """
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.kotlinx.serialization)
    id("kmp.koin.ksp")
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = "$packageName.$layer"
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
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
    }
}

            """.trimIndent()
    }

    fun data(packageName: String, layer: String = "data"): String {
        val featureName = packageName.split(".").last()
        return """
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room.schema)
    id("kmp.room.ksp")
    id("kmp.koin.ksp")
}

ksp {
    arg("room.schemaLocation", "${'$'}rootDir/data/database/schema")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

room {
    schemaDirectory("${'$'}rootDir/data/database/schema")
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = "$packageName.$layer"
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
            // Common deps

            // Feature deps
            implementation(projects.features.$featureName.domain)

            implementation(libs.room.runtime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
    }
}
            """.trimIndent()
    }

    fun ui(packageName: String, layer: String = "ui"): String {
        val featureName = packageName.split(".").last()
        return """
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinx.serialization)
    id("kmp.koin.ksp")
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = "$packageName.$layer"
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
        testCoverage {}
    }
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonMain.dependencies {
            // Common deps

            // Feature deps
            implementation(projects.features.$featureName.domain)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.material3.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.nav3)
            implementation(libs.compose.ui.tooling.preview)
        }
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling)
        }
    }
}
            """.trimIndent()
    }

    fun tests(packageName: String, layer: String = "tests"): String {
        val featureName = packageName.split(".").last()
        return """
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.multiplatform.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinx.serialization)
    id("kmp.koin.ksp")
}

kmpKoinKsp {
    useKoinViewModel = true
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = "$packageName.$layer"
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
        withHostTest {
            enableCoverage = true
            isIncludeAndroidResources = true
        }
        withDeviceTest {
            enableCoverage = true
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            execution = "HOST"
        }
        androidResources {
            enable = true
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        testCoverage {}
    }
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonTest.dependencies {

            implementation(projects.testCommon)
            implementation(projects.features.$featureName.domain)
            implementation(projects.features.$featureName.ui)
            implementation(projects.features.$featureName.data)
            implementation(projects.data.database)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.material3.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.nav3)

            implementation(libs.koin.core)
            implementation(libs.koin.test)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        getByName("androidDeviceTest") {
            dependencies {

                implementation(projects.testCommon)
                api(projects.features.$featureName.domain)
                api(projects.features.$featureName.ui)
                api(projects.features.$featureName.data)
                api(projects.data.database)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin.core)
                implementation(libs.koin.annotations)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.compose.material3.icons.extended)
                implementation(libs.compose.material3)
                implementation(libs.compose.nav3)

                implementation(libs.koin.test)
                implementation(libs.koin.core.viewmodel)
                implementation(libs.androidx.test.compose.manifest)
                implementation(libs.androidx.test.compose.junit)
                implementation(libs.androidx.test.junit)
                implementation(libs.androidx.test.espresso)
            }
        }
    }
}

dependencies {
    add("androidHostTestImplementation", libs.paparazzi.classgraph)
}
        """.trimIndent()
    }

}
