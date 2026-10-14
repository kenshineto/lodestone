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

internal fun Project.configureMavenPublish(pom: PublishArguments) {
	pluginManager.apply("com.vanniktech.maven.publish")
	pluginManager.apply("signing")

	extensions.configure<PublishingExtension> {
		publications.withType<MavenPublication>().configureEach {
			suppressAllPomMetadataWarnings()
		}
	}

	extensions.configure<MavenPublishBaseExtension> {
        coordinates(
            groupId = pom.group,
            artifactId = pom.artifact,
            version = version.toString()
        )
        publishToMavenCentral()
		signAllPublications()

		pom {
			name.set(rootProject.name)
			description.set(rootProject.description)
			url.set(pom.url)
			licenses {
        	    license {
        	        name.set(pom.license.title)
        	        url.set(pom.license.url)
        	    }
        	}
			developers {
        	    developer {
        	        id.set(pom.author)
        	        name.set(pom.author)
        	    }
        	}
        	scm {
        	    url.set(pom.url)
        	}
		}
    }

	extensions.configure<SigningExtension> {
		useGpgCmd()
	}
}
