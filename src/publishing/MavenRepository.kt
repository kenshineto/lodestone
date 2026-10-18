package cat.freya.lodestone.publishing

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import java.net.URI
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.authentication.http.BasicAuthentication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.support.uppercaseFirstChar

data class MavenRepository(var name: String = "maven", var url: String? = null, var redirect: Boolean = false) {
    private fun getUri(project: Project): URI {
        val url = url ?: error("maven repository url not set")

        if (!redirect) {
            return project.uri(url)
        }

        val isSnapshot = project.version.toString().endsWith("-SNAPSHOT", ignoreCase = true)
        val repo = if (isSnapshot) "snapshots" else "releases"
        return project.uri("$url/$repo")
    }

    private fun getCredential(project: Project, type: String): String? {
        val property = "${this.name}${type.uppercaseFirstChar()}"
        val envVar = "${this.name.uppercase()}_${type.uppercase()}"
        return project.findProperty(property) as? String ?: System.getenv(envVar)
    }

    internal fun apply(project: Project) {
        if (this == MAVEN_CENTERAL) {
            project.extensions.configure<MavenPublishBaseExtension> {
                publishToMavenCentral()
            }
            return
        }

        val me = this
        project.extensions.configure<PublishingExtension> {
            repositories {
                maven {
                    name = me.name
                    url = me.getUri(project)
                    credentials {
                        username = getCredential(project, "username")
                        password = getCredential(project, "password")
                    }
                    authentication {
                        create("basic", BasicAuthentication::class.java)
                    }
                }
            }
        }
    }

    companion object {
        val MAVEN_CENTERAL = MavenRepository("mavenCentral", "https://repo1.maven.org/maven2/", false)
    }
}
