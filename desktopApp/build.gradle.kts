import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.hot.reload)
}

kotlin {
    jvm()
    sourceSets {
        jvmMain.dependencies {
            implementation(projects.app)
            implementation(compose.desktop.currentOs)
            implementation(libs.compose.material3)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.compose.ui.tooling)
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "eu.caraus.kmp.samplearch"
            packageVersion =
                libs.versions.app.versionName
                    .get()
        }
    }
}
