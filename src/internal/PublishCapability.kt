package cat.freya.lodestone.internal

import org.gradle.api.publish.maven.MavenPublication
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import cat.freya.lodestone.PublishArguments
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.api.tasks.Exec

internal fun Project.configureMavenPublish(args: PublishArguments) {
	pluginManager.apply("com.vanniktech.maven.publish")
	pluginManager.apply("signing")

	val stagingRepo = layout.buildDirectory.dir("repo")

	extensions.configure<PublishingExtension> {
		publications.withType<MavenPublication>().configureEach {
			suppressAllPomMetadataWarnings()
		}

		repositories {
			maven {
				name = "staging"
				url = uri(stagingRepo)
			}
		}
	}

	extensions.configure<MavenPublishBaseExtension> {
        coordinates(
            groupId = args.group,
            artifactId = args.artifact,
            version = version.toString()
        )

		signAllPublications()

		pom {
			name.set(rootProject.name)
			description.set(rootProject.description)
			url.set(args.url)
			licenses {
        	    license {
        	        name.set(args.license.title)
        	        url.set(args.license.url)
        	    }
        	}
			developers {
        	    developer {
        	        id.set(args.author)
        	        name.set(args.author)
        	    }
        	}
        	scm {
        	    url.set(args.url)
        	}
		}
    }

	extensions.configure<SigningExtension> {
		useGpgCmd()
	}

	val syncTask = tasks.register("syncMavenRepository", Exec::class.java) {
		commandLine("rsync", "-az", "--", stagingRepo.get().asFile.absolutePath + "/", args.repo)
		onlyIf {
			tasks.named("publish").get().state.failure == null
		}
	}

	tasks.named("publish") {
		finalizedBy(syncTask)
	}
}
