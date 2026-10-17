package cat.freya.lodestone

import org.gradle.api.Plugin
import org.gradle.api.Project

// version of the plugin that doesn't apply kotlin
class LodestonePluginMinimal : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("lodestone", LodestoneExtension::class.java)
        extension.applyKotlin.set(false)
    }
}
