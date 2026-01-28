plugins {
    `kotlin-dsl`
}

//try this
// https://github.com/PaulWoitaschek/Voice/blob/main/plugins/build.gradle.kts

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // todo import from version catalog
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.0")
    implementation("com.android.tools.build:gradle-api:9.0.0")
    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:2.3.0")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.4")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:14.0.1") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
    }
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.8") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
    }
    implementation("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8") {

    }
    implementation("io.nlopez.compose.rules:ktlint:0.5.3") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
    }
    implementation("io.nlopez.compose.rules:detekt:0.4.23") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-compiler-embeddable")
    }
    implementation(gradleApi())
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
        create("koinKsp") {
            id = "kmp.koin.ksp"
            implementationClass = "eu.caraus.kmp.KMPKoinKsp"
        }
        create("roomKsp") {
            id = "kmp.room.ksp"
            implementationClass = "eu.caraus.kmp.KMPRoomKsp"
        }
        create("androidTestCoverage") {
            id = "app-android-test-coverage"
            implementationClass = "eu.caraus.kmp.KMPAndroidTestCoverage"
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
