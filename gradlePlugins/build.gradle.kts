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
    // todo check why warning on compiler version in the classpath
//  implementation(libs.ktlint.rules.compose)
//  implementation(libs.detekt.rules.formatting)
//  implementation(libs.detekt.rules.compose) {
//      exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
//   }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

gradlePlugin {
    plugins {
//        create("koinKsp") {
//            id = "kmp.koin.ksp"
//            implementationClass = "eu.caraus.kmp.KMPKoinKsp"
//        }
        create("roomKsp") {
            id = "kmp.room.ksp"
            implementationClass = "eu.caraus.kmp.KMPRoomKsp"
        }
        create("androidFeatureCoverageReport") {
            id = "app-feature-android-test-coverage"
            implementationClass = "eu.caraus.kmp.coverage.AndroidFeatureCoverageReport"
        }
        create("androidTotalCoverageReport") {
            id = "app-total-android-test-coverage"
            implementationClass = "eu.caraus.kmp.coverage.AndroidTotalCoverageReport"
        }
        create("kmpTotalCoverageReport") {
            id = "app-total-kmp-test-coverage"
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
