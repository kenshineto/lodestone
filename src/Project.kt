package cat.freya.lodestone

import cat.freya.lodestone.internal.Versions
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getByType

private fun formatVersion(version: String): String {
    return version.replace(".+", "").split("+")[0]
}

fun Project.getVersion(name: String): String {
    val libs = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
    return formatVersion(libs.findVersion(name).get().requiredVersion)
}

fun getKotlinVersion(): String {
    return formatVersion(Versions.KOTLIN)
}

val excludeKotlin: ExternalModuleDependency.() -> Unit = {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.jetbrains.kotlinx")
    exclude(group = "org.slf4j")
}

fun Project.isKotlinDslApplied(): Boolean {
    return pluginManager.hasPlugin("org.gradle.kotlin-dsl")
}
