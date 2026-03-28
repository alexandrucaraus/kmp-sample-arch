package eu.caraus.kmp.coverage

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.cc.base.logger
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.tasks.JacocoReport

/**
 * Generates coverage report per feature
 * depends on connectedAndroidTests running on emulator
 * and android unit tests
 * a feature must have the shape of :features:<YOUR_FEATURE_NAME>:(tests,domain,ui,...)
 * all test are defined in :features:<YOUR_FEATURE_NAME:tests module
 * this plugin generates reports based on tests module which usually depends on other modules from the feature
 */
class AndroidFeatureCoverageReport : Plugin<Project> {
    private val featuresCoverageReport = "androidFeaturesCoverageReport"
    private val featureModulesCoverageReport = "androidFeatureModulesCoverageReport"
    private val nonFeatureModuleCoverageReport = "androidNonFeatureModuleCoverageReport"

    val excluded = ExcludedModulesFromCoverage.excluded

    override fun apply(project: Project) {
        require(project == project.rootProject) {
            "Must apply to root project"
        }
        project.afterEvaluate {
            registerFeaturesModulesCoverageTask(project, features(project))
            registerNonFeatureModuleCoverageTask(project)
            registerFeaturesCoverageTask(project)
        }
    }

    private fun registerFeaturesCoverageTask(project: Project) {
        project.plugins.apply("jacoco")
        project.tasks.register(featuresCoverageReport) {
            dependsOn(
                project.subprojects
                    .mapNotNull { sub ->
                        sub.tasks.findByName(featureModulesCoverageReport) ?:
                            sub.tasks.findByName(nonFeatureModuleCoverageReport)
                    },
            )
        }
    }

    private fun registerFeaturesModulesCoverageTask(
        project: Project,
        features: Map<String, List<String>>
    ) {
        features.forEach { (_, featureModules) ->
            registerFeatureModulesCoverageTask(
                project, featureModules
            )
        }
    }

    private fun registerFeatureModulesCoverageTask(
        project: Project,
        featureModules: List<String>
    ) {
        val testModule = featureModules.find { it.contains("tests") }
        val targetModules = featureModules - testModule

        val testProject = project.subprojects.find { it.path == testModule } ?: error("No $testModule found")
        val targetProjects = project.subprojects.filter { it.path in targetModules }

        testProject.plugins.apply("jacoco")
        testProject.tasks.register<JacocoReport>(featureModulesCoverageReport) {

            group = "Reporting"
            description = "Feature level Android unit and instrumented coverage report"

            mustRunAfter(
                targetProjects.flatMap { subproject ->
                    subproject.tasks.filter { task ->
                        task.name.startsWith("compile") ||
                            task.name.startsWith("process") ||
                            task.name.startsWith("generate") ||
                            task.name.startsWith("package") ||
                            task.name.startsWith("merge") ||
                            task.name.startsWith("check")
                    }
                }
            )

            sourceDirectories.setFrom(
                targetProjects.map { subproject ->
                    subproject.files(
                        coverageSources
                            .map { path -> subproject.file(path) }
                            .filter { it.exists() }
                            .map { it.path }
                    )
                }
            )

            classDirectories.setFrom(
                targetProjects.map { subproject ->
                    subproject.fileTree(subproject.layout.buildDirectory) {
                        include(*coverageClasses)
                        exclude(*coverageExcludedClasses)
                    }
                }
            )

            executionData.setFrom(
                testProject.provider {
                    testProject.fileTree(testProject.layout.buildDirectory) {
                        include(*coverageOutput)
                    }.files
                }
            )

            reports {
                xml.required.set(true)
                html.required.set(true)
            }
        }
    }

    private fun registerNonFeatureModuleCoverageTask(project: Project) {
        val nonFeatureModules = project.subprojects
            .exclude(excluded)
            .filterNot {
                it.path.startsWith(":features")
            }
        nonFeatureModules.forEach { module ->
            module.plugins.apply("jacoco")
            module.tasks.register<JacocoReport>(nonFeatureModuleCoverageReport) {
                group = "Reporting"
                description = "Non feature module Android unit and instrumented coverage report"

                mustRunAfter(
                    module.tasks.filter { task ->
                        task.name.startsWith("compile") ||
                            task.name.startsWith("process") ||
                            task.name.startsWith("generate") ||
                            task.name.startsWith("package") ||
                            task.name.startsWith("merge") ||
                            task.name.startsWith("check")
                    }
                )

                sourceDirectories.setFrom(
                    module.files(
                        coverageSources
                            .map { path -> module.file(path) }
                            .filter { it.exists() }
                            .map { it.path }
                    )
                )

                classDirectories.setFrom(
                    module.fileTree(module.layout.buildDirectory) {
                        include(*coverageClasses)
                        exclude(*coverageExcludedClasses)
                    }
                )

                executionData.setFrom(
                    module.fileTree(module.layout.buildDirectory) {
                        include(*coverageOutput)
                    }.files
                )

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }
            }
        }
    }

    private fun features(project: Project): Map<String, List<String>> {
        val features = project.subprojects
            .filter {
                it.path.matches("^:features(:[^:]+){2}$".toRegex())
            }
            .mapNotNull { sub ->
                sub.displayName.split(":").getOrNull(2)
            }
            .distinct()

        val featureAndModules: Map<String, List<String>> = features.associateWith { feature ->
            val featureModules = project.subprojects.filter { sub ->
                sub.path.matches(":features:$feature(:[^:]+)$".toRegex())
            }.map { sub ->
                sub.path
            }
            featureModules
        }

        return featureAndModules
    }

    private fun Set<Project>.exclude(modules: Set<String>) =
        filterNot { it.name in modules }


    val coverageSources = arrayOf(
        "src/commonMain/kotlin",
        "src/androidMain/kotlin",
        "src/main/kotlin",
        "src/main/java",
    )

    val coverageClasses = arrayOf(
        "**/classes/kotlin/**"
    )

    val coverageExcludedClasses = arrayOf(
        "**/*Preview*.*",
        "**/ksp/generated/**",
        "**/tests/**/*.*"
    )

    val coverageOutput = arrayOf(
        "**/outputs/unit_test_code_coverage/**/*.exec",
        "**/outputs/code_coverage/**/*.ec",
    )

    private fun print(msg: String) {
        logger.lifecycle(msg)
    }
}
