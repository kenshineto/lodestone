package cat.freya.lodestone.internal

import cat.freya.lodestone.LodestonePlugin
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal fun Project.configureDetekt() {
    pluginManager.apply("dev.detekt")

    val configFile = rootProject.layout.buildDirectory.file("lodestone/detekt.yml").get().asFile
    if (!configFile.exists()) {
        configFile.parentFile.mkdirs()
        LodestonePlugin.getResource("detekt.yml").use { input ->
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
