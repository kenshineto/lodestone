package cat.freya.lodestone.shadow

import cat.freya.lodestone.kotlin.KotlinVersions
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources

internal fun Project.initalizeShadow() {
    // fix "shadow" not available in dependencies {}
    project.pluginManager.apply("com.gradleup.shadow")

    tasks.named<ShadowJar>("shadowJar") {
        enabled = false
    }
}

internal fun Project.configureShadow(args: ShadowArguments, kotlinVersions: KotlinVersions) {
    val shadow = configurations.named("shadow").get()

    if (args.includeKotlin) {
        kotlinVersions.apply(this, "shadow")
    }

    val project = this
    tasks.withType<ShadowJar>().configureEach {
        enabled = true

        val jarName = rootProject.name
        val jarVersion = rootProject.version.toString()
        val jarPlatform = project.name

        // calculate jar name
        archiveBaseName.set(jarName)
        archiveVersion.set(jarVersion)
        archiveClassifier.set(jarPlatform)
        destinationDirectory.set(project.layout.buildDirectory.dir("libs"))
        if (args.includeProjectName) {
            archiveFileName.set("$jarName-$jarVersion+$jarPlatform.jar")
        } else {
            archiveFileName.set("$jarName-$jarVersion.jar")
        }

        // we need to process resources before
        // putting them in the jar
        dependsOn(tasks.named<ProcessResources>("processResources"))

        // only include shadow'd depends (not implementation)
        configurations.set(listOf(shadow))

        // multiple database drivers may collide here
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        mergeServiceFiles { include("META-INF/services/java.sql.Driver") }

        // remove META-INF crap
        exclude {
            it.path.startsWith("META-INF/") &&
                !it.path.startsWith("META-INF/services/") &&
                !it.path.endsWith(".kotlin_module") &&
                !it.path.endsWith(".yml") &&
                !it.path.endsWith(".json") &&
                !it.path.endsWith(".toml")
        }

        // relocate shaded deps
        args.relocations.forEach { pkg ->
            runCatching {
                // try to relocate and ignore on failure
                val module = pkg.split('.').last()
                relocate(pkg, "${rootProject.group}.depend.$module")
            }
        }
    }

    tasks.withType<ShadowJar>().all {
        tasks.findByName("assemble")?.dependsOn(this)
    }
}
