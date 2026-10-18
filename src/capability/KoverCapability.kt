package cat.freya.lodestone.capability

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKover() {
    pluginManager.apply("org.jetbrains.kotlinx.kover")

    if (this != rootProject) return

    dependencies {
        subprojects.forEach {
            "kover"(project(it.path))
        }
    }

    val coverage = tasks.findByName("coverage") ?: tasks.register("coverage").get()
    coverage.dependsOn("koverHtmlReport")
}
