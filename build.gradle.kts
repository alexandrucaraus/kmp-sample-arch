import com.android.build.gradle.tasks.factory.AndroidUnitTest


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.multiplatform.android.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.modulegraph) apply true
    id("app-total-android-test-coverage") apply true
    alias(libs.plugins.kover)
    id("app-feature-android-test-coverage") apply true
    id("app-total-kmp-test-coverage") apply false
    id("kmp.linter") apply true
    id("kmp.feature.skeleton") apply false
}

// Design system
// https://atomicdesign.bradfrost.com/chapter-2/

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "### Modules Structure"
    rootModulesRegex.set(":app")
    excludedModulesRegex.set(".*test.*")
    nestingEnabled = true
    showFullPath = true
    includeIsolatedModules = true
    setStyleByModuleType = true
}

tasks.register<Delete>("clean") {
    group = "build"
    description = "Deletes the build directory and all submodule build directories"

    // Delete root build directory
    delete(rootProject.layout.buildDirectory)

    // Delete all subproject build directories
    rootProject.subprojects {
        delete(layout.buildDirectory)
        delete(layout.projectDirectory.file(".gradle"))
    }
}

dependencies {
    rootProject.subprojects.toList()
        .filter {
            it.buildFile.exists()
        }
        .forEach {
            kover(project(it.path))
        }
}

subprojects {
    pluginManager.withPlugin("com.android.application") {
        apply(plugin = "org.jetbrains.kotlinx.kover")
        configure<kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension> {
            currentProject {
                createVariant("custom") {
                    add("debug")
                }
            }
        }
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        pluginManager.withPlugin("com.android.kotlin.multiplatform.library") {
            apply(plugin = "org.jetbrains.kotlinx.kover")
            configure<kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension> {
                currentProject {
                    createVariant("custom") {
                        add("android")
                    }
                }
            }
        }
    }
}

kover {
    currentProject {
        createVariant("custom") {}
    }
}

subprojects {
    tasks.withType<Test>().configureEach {
        (this as? AndroidUnitTest)?.variantName = "android"
    }
}
