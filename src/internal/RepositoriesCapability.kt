package cat.freya.lodestone.internal

import org.gradle.api.Project
import org.gradle.kotlin.dsl.mavenCentral
import org.gradle.kotlin.dsl.repositories

internal fun Project.configureRepositories() {
	repositories {
		gradlePluginPortal()
		mavenCentral()
	}
}
