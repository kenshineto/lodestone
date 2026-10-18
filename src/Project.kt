package cat.freya.lodestone

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getByType

val excludeKotlin: ExternalModuleDependency.() -> Unit = {
    exclude(group = "org.jetbrains.kotlin")
    exclude(group = "org.jetbrains.kotlinx")
    exclude(group = "org.slf4j")
}

fun Project.getVersion(name: String): String {
    val libs = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
    return libs.findVersion(name).get().requiredVersion.replace(".+", "").split("+")[0]
}
