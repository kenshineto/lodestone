package cat.freya.lodestone.capability

import cat.freya.lodestone.LodestonePlugin
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import java.io.InputStream
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

private fun getResource(path: String): InputStream {
    return LodestonePlugin::class.java.classLoader.getResourceAsStream(path)
        ?: throw IllegalArgumentException("Resource not found: $path")
}

internal fun Project.configureDetekt() {
    pluginManager.apply("dev.detekt")

    val configFile = rootProject.layout.buildDirectory.file("lodestone/detekt.yml").get().asFile
    if (!configFile.exists()) {
        configFile.parentFile.mkdirs()
        getResource("detekt.yml").use { input ->
            configFile.outputStream().use { output -> input.copyTo(output) }
        }
    }

    extensions.configure<DetektExtension> {
        config.setFrom(configFile)
        source.setFrom("src", "test")
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            html.required.set(false)
            checkstyle.required.set(false)
            sarif.required.set(false)
            markdown.required.set(false)
        }
    }
}
