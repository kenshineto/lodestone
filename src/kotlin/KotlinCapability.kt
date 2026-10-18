package cat.freya.lodestone.kotlin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal fun Project.configureKotlin(javaVersion: Int, kotlinVersions: KotlinVersions) {
    kotlinVersions.apply(this, "compileOnly")
    kotlinVersions.apply(this, "testImplementation")

    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain(javaVersion)
        sourceSets.named("main") {
            kotlin.srcDirs("src")
        }
        sourceSets.named("test") {
            kotlin.srcDirs("test")
        }
    }
}
