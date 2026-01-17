@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        includeBuild("gradlePlugins")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        includeBuild("gradlePlugins")
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "KMP_Sample_Arch"

include(":androidApp")
include(":app")
include(":data:database")
include(":features:notes:domain")
include(":features:notes:data")
include(":features:notes:ui")
include(":features:notes:itest")
include(":test-common")
