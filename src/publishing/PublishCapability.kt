package cat.freya.lodestone.publishing

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension

internal fun Project.configureMavenPublish(args: PublishArguments) {
    pluginManager.apply("com.vanniktech.maven.publish")
    pluginManager.apply("signing")

    val name = args.name ?: rootProject.name
    val artifact = args.artifact ?: this.name
    val group = args.group ?: this.group.toString()
    val author = args.author ?: error("publishing author not set")
    val license = args.license ?: error("publishing license not set")
    val gitRepo = args.gitRepo ?: error("publishing gitRepo not set")
    var website = args.website ?: gitRepo

    val project = this
    args.repositories.forEach { it.apply(project) }

    extensions.configure<PublishingExtension> {
        publications.withType<MavenPublication>().configureEach {
            suppressAllPomMetadataWarnings()
        }
    }

    extensions.configure<MavenPublishBaseExtension> {
        coordinates(
            groupId = group,
            artifactId = artifact,
            version = project.version.toString(),
        )

        if (args.sign) {
            signAllPublications()
        }

        pom {
            this.name.set(name)
            description.set(project.description ?: rootProject.description)
            url.set(website)
            licenses {
                license {
                    this.name.set(license.title)
                    url.set(license.url)
                }
            }
            developers {
                developer {
                    id.set(author)
                    this.name.set(author)
                }
            }
            scm {
                url.set(gitRepo)
            }
        }
    }

    extensions.configure<SigningExtension> {
        if (args.sign) {
            useGpgCmd()
        }
    }
}
