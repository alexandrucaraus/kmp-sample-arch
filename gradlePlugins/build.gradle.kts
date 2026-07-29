plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation(libs.kotlin.gradle.plugin.dev)
    implementation(libs.agp.gradle.plugin.dev)
    implementation(libs.multiplatform.gradle.plugin.dev)
    implementation(libs.google.ksp.plugin.dev)
    implementation(libs.ktlint.gradle.plugin.dev)
    implementation(libs.detekt.gradle.plugin.dev)
    implementation(libs.kover.gradle.plugin.dev)
    implementation(libs.asm)
    implementation(libs.androidx.room.gradle.plugin)
    // todo check why warning on compiler version in the classpath
//  implementation(libs.ktlint.rules.compose)
//  implementation(libs.detekt.rules.formatting)
//  implementation(libs.detekt.rules.compose) {
//      exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
//   }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

gradlePlugin {
    plugins {
        create("roomKsp") {
            id = "kmp.room.ksp"
            implementationClass = "eu.caraus.kmp.KMPRoomKsp"
        }
        create("androidFeatureCoverageReport") {
            id = "app-android-feature-test-coverage"
            implementationClass = "eu.caraus.kmp.coverage.AndroidFeatureCoverageReport"
        }
        create("androidTotalCoverageReport") {
            id = "app-android-total-test-coverage"
            implementationClass = "eu.caraus.kmp.coverage.AndroidTotalCoverageReport"
        }
        create("kmpTotalCoverageReport") {
            id = "app-kmp-total-test-coverage"
            implementationClass = "eu.caraus.kmp.coverage.KmpTotalCoverageReport"
        }
        create("linter") {
            id = "kmp.linter"
            implementationClass = "eu.caraus.kmp.KMPKtLinter"
        }
        create("featureSkeleton") {
            id = "kmp.feature.skeleton"
            implementationClass = "eu.caraus.kmp.KMPKtLinter"
        }
    }
}
