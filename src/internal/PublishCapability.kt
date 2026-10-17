package cat.freya.lodestone.internal

import cat.freya.lodestone.PublishArguments
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.authentication.http.BasicAuthentication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension

private fun Project.getSecret(name: String, default: String): String {
    return findProperty(name) as? String ?: System.getenv(name) ?: default
}

internal fun Project.configureMavenPublish(args: PublishArguments) {
    pluginManager.apply("com.vanniktech.maven.publish")
    pluginManager.apply("signing")

    extensions.configure<PublishingExtension> {
        publications.withType<MavenPublication>().configureEach {
            suppressAllPomMetadataWarnings()
        }

        repositories {
            maven {
                val isSnapshot = project.version.toString().endsWith("-SNAPSHOT", ignoreCase = true)
                val slug = if (isSnapshot) "snapshots" else "releases"

                url = uri("${args.repo}/$slug")
                credentials {
                    username = getSecret("mavenUsername", args.username)
                    password = getSecret("mavenPassword", args.password)
                }
                authentication {
                    create(args.auth, BasicAuthentication::class.java)
                }
            }
        }
    }

    extensions.configure<MavenPublishBaseExtension> {
        coordinates(
            groupId = args.group,
            artifactId = args.artifact,
            version = version.toString(),
        )

        if (args.sign) {
            signAllPublications()
        }

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
        if (args.sign) {
            useGpgCmd()
        }
    }
}
