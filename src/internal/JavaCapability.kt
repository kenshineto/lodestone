package cat.freya.lodestone.internal

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named

internal fun Project.configureJava(javaVersion: Int) {
    pluginManager.apply("java-library")

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }

    val srcDir = layout.projectDirectory.dir("src").asFile
    if (!srcDir.exists()) {
        // disable jar, no source code
        tasks.named<Jar>("jar") { enabled = false }
    }
}
