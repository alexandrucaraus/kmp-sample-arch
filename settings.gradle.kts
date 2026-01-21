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

include(":test-common")


include(":androidApp")
include(":app")

// App common db
include(":data:database")

// Feature notes
include(":features:notes:domain")
include(":features:notes:data")
include(":features:notes:ui")
include(":features:notes:tests")
