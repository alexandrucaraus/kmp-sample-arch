package eu.caraus.kmp.feature.skeleton

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File

abstract class CreateFeatureModuleTask : DefaultTask() {

    @Option(
        option = "featureName",
        description = "Name of the feature (module)"
    )
    @Input
    var featureName: String? = null

    @Option(
        option = "packageName",
        description = "kotlin package name"
    )
    @Input
    var packageName: String? = null

    @Option(
        option = "layer",
        description = "domain, ui, data, tests or all (all previous)",
    )
    @Input
    var layer: String = "all"

    @TaskAction
    fun create() {

        val featureName = featureName
            ?: throw GradleException("Requires feature name e.g: --featureName=notes")

        val packageName = packageName
            ?: throw GradleException("Requires package name e.g: --packageName=org.stuff.io")

        val layer = layer.also {
            if (it !in listOf("domain", "ui", "data", "tests", "all")) {
                throw GradleException("layer should be one of: domain, ui, data, tests, all")
            }
        }

        val featureDir = File("./", "features/$featureName").also {
            it.makedirs()
        }

        createDomainModule(featureDir = featureDir)

        when (layer) {

            "domain" -> {
                updateProjectSettingsModules(listOf("domain"))
            }

            "ui" -> {
                createUiModule(featureDir = featureDir)
                updateProjectSettingsModules(listOf("domain", "ui"))
            }

            "data" -> {
                createDataModule(featureDir = featureDir,)
                updateProjectSettingsModules(listOf("domain", "data"))
            }

            "tests", "all" -> {
                createUiModule(featureDir = featureDir)
                createDataModule(featureDir = featureDir)
                createTestsModule(featureDir = featureDir)
                updateProjectSettingsModules(listOf("domain", "ui", "data", "tests"))
            }
        }
    }

    private fun createDomainModule(
        featureDir: File,
        layer: String = "domain",
    ) {
        val domainModuleDir = File(featureDir, layer).also {
            if (!it.exists()) it.mkdirs()
        }

        File(domainModuleDir, "build.gradle.kts")
            .writeText(BuildScriptsTemplates.domain(packageName!!))

        val packageNamePath =
            packageName.plus(".$layer").replace(".", "/")

        listOf(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin"
        ).forEach { sourceSet ->
            File(domainModuleDir, "$sourceSet/$packageNamePath").makedirs()
        }
    }

    private fun createUiModule(
        featureDir: File,
        layer: String = "ui",
    ) {
        val uiModuleDir = File(featureDir, layer).also {
            if (!it.exists()) it.mkdirs()
        }

        File(uiModuleDir, "build.gradle.kts")
            .writeText(BuildScriptsTemplates.ui(packageName!!))

        val packageNamePath = packageName.plus(".$layer").replace(".", "/")

        listOf(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin"
        ).forEach { sourceSet ->
            File(uiModuleDir, "$sourceSet/$packageNamePath").makedirs()
        }
    }

    private fun createDataModule(
        featureDir: File, layer: String = "data",
    ) {
        val dataModuleDir = File(featureDir, layer).also {
            if (!it.exists()) it.mkdirs()
        }

        File(dataModuleDir, "build.gradle.kts").writeText(
            BuildScriptsTemplates.data(packageName!!, layer)
        )

        val packageNamePath = packageName.plus(".$layer").replace(".", "/")

        listOf(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin"
        ).forEach { sourceSet ->
            File(dataModuleDir, "$sourceSet/$packageNamePath").makedirs()
        }
    }


    fun createTestsModule(
        featureDir: File, layer: String = "tests",
    ) {
        val testModuleDir = File(featureDir, layer).also {
            if (!it.exists()) it.mkdirs()
        }

        File(testModuleDir, "build.gradle.kts").writeText(
            BuildScriptsTemplates.data(packageName!!, layer)
        )

        val packageNamePath = packageName.plus(".$layer").replace(".", "/")

        listOf(
            "src/commonTest/kotlin",
            "src/androidHostTest/kotlin",
            "src/androidDeviceTest/kotlin"
        ).forEach { sourceSet ->
            File(testModuleDir, "$sourceSet/$packageNamePath").makedirs()
        }
    }

    fun updateProjectSettingsModules(
        layers: List<String> = listOf("domain", "ui", "data", "test")
    ) {
        val settingsFile = File("./", "settings.gradle.kts")

        val includes = layers.map { layer ->
            ":features:$featureName:$layer"
        }

        val content = settingsFile.readText()

        val newLines = includes
            .filterNot { content.contains("include(\"$it\")") }
            .joinToString("\n") { "include(\"$it\")" }

        if (newLines.isNotBlank()) {
            settingsFile.appendText("\n\n// Feature $featureName\n$newLines")
            println("✔ settings.gradle.kts updated. Re-run Gradle.")
        } else {
            println("ℹ Feature already included in settings.gradle.kts")
        }
    }

    private fun File.makedirs() {
        fun mkdirs(
            file: File,
            orig: File,
        ) {
            if (orig.exists()) return

            if (!file.parentFile.exists()) {
                mkdirs(file.parentFile, orig)
            } else {
                file.mkdir()
                mkdirs(orig, orig)
            }
        }
        mkdirs(this, this)
    }

}
