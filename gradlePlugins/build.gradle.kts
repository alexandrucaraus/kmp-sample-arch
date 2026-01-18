plugins {
    `kotlin-dsl`
}

//try this
// https://github.com/PaulWoitaschek/Voice/blob/main/plugins/build.gradle.kts

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.0")
    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:2.3.0")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.4")
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
        create("jacoco") {
            id = "kmp.jacoco"
            implementationClass = "eu.caraus.kmp.KMPJacoco"
        }
    }
}
