import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.Exec

plugins {
    `kotlin-dsl`
    signing
    alias(libs.plugins.vanniktech.publish)
}

group = "cat.freya.lodestone"
version = "0.1-SNAPSHOT"
description = "Gradle plugin for KenshinEto's projects"

val author = "KenshinEto"
val scm = "https://g.freya.cat/lodestone"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kover.gradle.plugin)
    implementation(libs.shadow.gradle.plugin) {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation(libs.spotless.gradle.plugin)
    implementation(libs.vanniktech.gradle.plugin)
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDirs("src")
            resources.srcDirs("res")
        }
    }
}

gradlePlugin {
    website.set(scm)
    vcsUrl.set(scm)

    plugins {
        create("lodestone") {
            id = rootProject.group.toString()
            implementationClass = "${rootProject.group}.LodestonePlugin"
            displayName = rootProject.name
            description = rootProject.description
            tags.set(listOf("minecraft", "lodestone"))
        }
    }
}

extensions.configure<PublishingExtension> {
    publications.withType<MavenPublication>().configureEach {
        suppressAllPomMetadataWarnings()
    }
}

val stagingRepo = layout.buildDirectory.dir("repo")
val remoteRepo = "maven.in.freya.cat:/nfs/maven"

publishing {
    repositories {
		maven {
			name = "staging"
			url = uri(stagingRepo)
		}
	}
}

mavenPublishing {
    coordinates(
        groupId = "cat.freya",
        artifactId = "lodestone-gradle-plugin",
        version = version.toString(),
    )

    signAllPublications()

    pom {
        name.set(rootProject.name)
        description.set(rootProject.description)
        url.set(scm)
        licenses {
            license {
                name.set("GNU General Public License v3.0")
                url.set("https://gnu.org")
            }
        }
        developers {
            developer {
                id.set(author)
                name.set(author)
            }
        }
        scm {
            url.set(scm)
        }
    }
}

signing {
    useGpgCmd()
}

val syncTask = tasks.register<Exec>("syncMavenRepository") {
commandLine("rsync", "-az", "--", stagingRepo.get().asFile.absolutePath + "/", remoteRepo)
	onlyIf {
		tasks.named("publish").get().state.failure == null
	}
}

tasks.named("publish") {
	finalizedBy(syncTask)
}
