package cat.freya.lodestone.capability

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources

internal fun Project.configureResources(resources: Array<out String>, buildInfo: Map<String, Any>) {
    tasks.named<ProcessResources>("processResources").configure {
        inputs.properties(buildInfo)
        resources.forEach { resource ->
            filesMatching(resource) { expand(buildInfo) }
        }
    }
}
