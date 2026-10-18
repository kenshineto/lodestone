package cat.freya.lodestone.capability

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named

internal fun Project.configureJava(javaVersion: Int) {
    pluginManager.apply("java-library")

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion))

        sourceSets {
            named("main") {
                java.srcDirs("src")
                resources.srcDirs("res")
            }
            named("test") {
                java.srcDirs("test")
            }
        }
    }
}
