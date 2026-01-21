package eu.caraus.kmp.feature.skeleton

import org.gradle.api.Plugin
import org.gradle.api.Project

class KMPFeatureSkeleton : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register(
            "createFeatureModule",
            CreateFeatureModuleTask::class.java
        )
    }
}
