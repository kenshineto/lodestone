package cat.freya.lodestone

import org.gradle.api.Plugin
import org.gradle.api.Project

class LodestonePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("lodestone", LodestoneExtension::class.java)
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
    }
}
