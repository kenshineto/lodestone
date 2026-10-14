package cat.freya.lodestone

import cat.freya.lodestone.internal.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.InputStream

class LodestonePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("lodestone", LodestoneExtension::class.java)
        project.initalizeShadow()
    }

    companion object {
        internal fun getResource(path: String): InputStream {
            return LodestonePlugin::class.java.classLoader.getResourceAsStream(path)
                ?: throw IllegalArgumentException("Resource not found: $path")
        }
    }
}
