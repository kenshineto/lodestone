package cat.freya.lodestone.capability

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.named

internal fun Project.configureTasks() {
    if (this != rootProject) return

    // dont check on builds
    tasks.named("build") {
        dependsOn.removeIf { it.toString().contains("check") }
    }

    // disable jar if no source
    val srcDir = layout.projectDirectory.dir("src").asFile
    if (!srcDir.exists()) {
        // disable jar, no source code
        tasks.named<Jar>("jar") { enabled = false }
    }

    // link up lint and format tasks
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
