package cat.freya.lodestone.internal

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal fun Project.configureKotlin(javaVersion: Int) {
    dependencies {
        "compileOnly"(Dependencies.kotlinStdlib)
        "compileOnly"(Dependencies.kotlinReflect)
        "compileOnly"(Dependencies.kotlinxCoroutines)
    }

    extensions.configure<KotlinJvmProjectExtension> {
        jvmToolchain(javaVersion)
        sourceSets.named("main") {
            kotlin.srcDirs("src")
            resources.srcDirs("res")
        }
        sourceSets.named("test") {
            kotlin.srcDirs("test")
        }
    }

    // dont check on builds
    tasks.named("build") {
        dependsOn.removeIf { it.toString().contains("check") }
    }
}
