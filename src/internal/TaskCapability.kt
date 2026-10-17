package cat.freya.lodestone.internal

import org.gradle.api.Project

internal fun Project.configureTasks() {
    if (this != rootProject) return

    val lint = tasks.findByName("lint") ?: tasks.register("lint").get()
    val format = tasks.findByName("format") ?: tasks.register("format").get()

    afterEvaluate {
        allprojects.forEach { project ->
            // lint
            project.tasks.findByName("spotlessCheck")?.let { lint.dependsOn(it) }
            project.tasks.findByName("detekt")?.let { lint.dependsOn(it) }
            // format
            project.tasks.findByName("spotlessApply")?.let { format.dependsOn(it) }
        }
    }
}
