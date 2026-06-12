package eu.caraus.kmp.coverage

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class KmpTotalCoverageReport : Plugin<Project> {

    private val rootCoverageTaskName = "kmpTotalCoverageReport"
    private val appCoverageTaskName = "kmpAppCoverageReport"
    private val featuresCoverageTaskName = "kmpFeaturesCoverageReport"
    private val otherCoverageTaskName = "kmpOtherCoverageReport"

    // Only test-common is fully excluded; the "tests" module needs Kover applied so its
    // testAndroidHostTest task gets the Kover agent attached (otherwise no .ic files → 0% coverage).
    private val koverPluginExcluded = setOf("test-common")
    val excludedProjects = ExcludedModulesFromCoverage.excluded

    override fun apply(project: Project) {
        require(project == project.rootProject) {
            "Must apply to root project"
        }
        project.plugins.apply("org.jetbrains.kotlinx.kover")
        project.dependencies {
            project.subprojects.toList()
                .filter {
                    it.buildFile.exists() && it.name !in koverPluginExcluded
                }
                .forEach {
                    "kover"(project(it.path))
                }
        }
        project.extensions.configure<KoverProjectExtension>("kover") {
            currentProject {
                createVariant("custom") {}
                copyVariant("app", "custom")
                copyVariant("features", "custom")
                copyVariant("other", "custom")
            }
            reports {
                // Exclude test-infrastructure classes and @Composable @Preview functions globally
                filters {
                    excludes {
                        packages(testModulePackages(project))
                        annotatedBy("androidx.compose.ui.tooling.preview.Preview")
                    }
                }
                variant("app") {
                    filters {
                        includes {
                            packages("eu.caraus.kmp.samplearch")
                        }
                    }
                }
                variant("features") {
                    filters {
                        includes {
                            packages(featurePackages(project))
                        }
                        excludes {
                            packages(testModulePackages(project))
                        }
                    }
                }
                variant("other") {
                    filters {
                        excludes {
                            packages(listOf("eu.caraus.kmp.samplearch") + featurePackages(project) + testModulePackages(project))
                        }
                    }
                }
            }
        }
        applySubProjectsPlugin(project)
        configureRootTask(project)
        configurePerCategoryTasks(project)
    }

    private fun featurePackages(project: Project): List<String> {
        return project.subprojects
            .filter { it.path.matches("^:features(:[^:]+){2}$".toRegex()) }
            .mapNotNull { it.path.split(":").getOrNull(2) }
            .distinct()
            .map { "eu.caraus.kmp.$it" }
    }

    // Returns packages belonging to "tests" modules (test infrastructure, not production code)
    private fun testModulePackages(project: Project): List<String> {
        return project.subprojects
            .filter { it.name == "tests" }
            .mapNotNull { it.path.split(":").getOrNull(2) }
            .distinct()
            .map { "eu.caraus.kmp.$it.tests" }
    }

    private fun configureRootTask(project: Project) {
        project.tasks.register(rootCoverageTaskName) {
            group = "Reporting"
            description = "Kover total coverage report"
            dependsOn("koverHtmlReport", "koverXmlReport")
        }
    }

    private fun configurePerCategoryTasks(project: Project) {
        project.tasks.register(appCoverageTaskName) {
            group = "Reporting"
            description = "Kover app modules coverage report"
            dependsOn("koverXmlReportApp")
        }
        project.tasks.register(featuresCoverageTaskName) {
            group = "Reporting"
            description = "Kover feature modules coverage report"
            dependsOn("koverXmlReportFeatures")
        }
        project.tasks.register(otherCoverageTaskName) {
            group = "Reporting"
            description = "Kover other modules coverage report"
            dependsOn("koverXmlReportOther")
        }
    }

    private fun applySubProjectsPlugin(project: Project) {
        project.subprojects.filterNot { it.name in koverPluginExcluded }.forEach { sub ->
            with(sub) {
                pluginManager.withPlugin("com.android.application") {
                    apply(plugin = "org.jetbrains.kotlinx.kover")
                    configure<KoverProjectExtension> {
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
                        configure<KoverProjectExtension> {
                            currentProject {
                                createVariant("custom") {
                                    // "tests" modules run all tests via testAndroidHostTest;
                                    // use the androidHostTest compilation so Kover attaches
                                    // its agent to that task and generates .ic coverage data.
                                    add("android")
                                }
                            }
                        }
                    }
                    // JVM-only KMP modules (e.g. desktopApp) never apply the Android library
                    // plugin, so the branch above is skipped; the android check must happen
                    // after evaluation because plugins are applied in declaration order.
                    afterEvaluate {
                        if (!pluginManager.hasPlugin("com.android.kotlin.multiplatform.library")) {
                            apply(plugin = "org.jetbrains.kotlinx.kover")
                            configure<KoverProjectExtension> {
                                currentProject {
                                    createVariant("custom") {
                                        add("jvm")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
