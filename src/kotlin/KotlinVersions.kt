package cat.freya.lodestone.kotlin

import cat.freya.lodestone.Dependencies
import cat.freya.lodestone.Versions
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinVersions {
    var kotlinStdlib: String = Dependencies.kotlinStdlib(Versions.KOTLIN)
    var kotlinReflect: String = Dependencies.kotlinReflect(Versions.KOTLIN)
    val kotlinxCoroutines: String = Dependencies.KOTLINX_COROUTINES

    fun set(kotlinVersion: String) {
        kotlinStdlib = Dependencies.kotlinStdlib(kotlinVersion)
        kotlinReflect = Dependencies.kotlinReflect(kotlinVersion)
    }

    internal fun apply(project: Project, type: String) {
        project.dependencies {
            add(type, kotlinStdlib)
            add(type, kotlinReflect)
            add(type, kotlinxCoroutines)
        }
    }
}
