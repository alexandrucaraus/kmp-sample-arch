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

include(":features:notes:domain")
include(":features:notes:data")
include(":features:notes:ui")

include(":data:database")
