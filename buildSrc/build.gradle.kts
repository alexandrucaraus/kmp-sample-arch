plugins {
    `kotlin-dsl`
   // kotlin("multiplatform")
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Add dependencies for plugins you want to use in your convention plugins
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.2.21")
    //implementation("com.android.tools.build:gradle:8.13.1")
}
