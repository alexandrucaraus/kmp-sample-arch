@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        includeBuild("gradlePlugins")
        mavenLocal()
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

dependencyResolutionManagement {
    repositories {
        includeBuild("gradlePlugins")
        mavenLocal()
        google()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
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

