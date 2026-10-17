package cat.freya.lodestone

import cat.freya.lodestone.internal.*
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.kotlin.dsl.support.getBooleanKotlinDslOption

abstract class LodestoneExtension @Inject constructor(private val project: Project) {
    abstract val buildInfoValues: MapProperty<String, Any>
    private val shadowArguments = ShadowArguments()

    init {
        buildInfoValues.convention(emptyMap())
        project.pluginManager.apply("org.jetbrains.kotlin.jvm")
    }

    /**
     * Configure the gradle project with a given java version
     *
     * @param srcVersion The jvm version to use for source code (default 25)
     * @param testVersion The jvm version to use for tests (default 25)
     */
    fun jvm(srcVersion: Int = Versions.JAVA, testVersion: Int = Versions.JAVA) {
        with(project) {
            configureRepositories()
            configureJava(srcVersion)
            configureKotlin(srcVersion)
            configureTests(testVersion)
            configureTasks()
        }
    }

    /**
     * Set build info information
     */
    fun buildInfo(fn: MutableMap<String, Any>.() -> Unit) {
        val map = mutableMapOf<String,Any>()
        fn(map)
        buildInfoValues.convention(map)
    }

    /**
     * Generate a BuildInfo.kt in the root projects group
     */
    fun generateBuildInfo() {
        project.configureBuildInfo(buildInfoValues.get())
    }


    /**
     * Process given resources in the 'res' directory
     *
     * @param resources The list of resources to process with the provided build info
     */
    fun resources(vararg resources: String) {
        project.configureResources(resources, buildInfoValues.get())
    }

    /**
     * Create a shadowed jar for the project
     *
     * @param fn Lambda to specify shadow jar arguments
     */
    fun shadow(fn: ShadowArguments.() -> Unit = {}) {
        shadow(true, fn)
    }

    /**
     * Create a shadowed jar for the project
     *
     * @param apply set to false to not apply shadow jar yet, just configure
     * @param fn Lambda to specify shadow jar arguments
     */
    fun shadow(apply: Boolean = true, fn: ShadowArguments.() -> Unit) {
        fn(shadowArguments)
        if (apply) {
            project.configureShadow(shadowArguments)
        }
    }

    /**
     * Include detekt linting into the gradle project
     */
    fun detekt() {
        project.configureDetekt()
    }

    /**
     * Include spotless formatting into the gradle project
     */
    fun spotless(ktfmtVersion: String = Versions.KTFMT) {
        project.configureSpotless(ktfmtVersion)
    }

    /**
     * Include kover code coverage into the gradle project
     */
    fun kover() {
        project.configureKover()
    }

    /**
     * Support publishing to maven centeral
     *
     * @param artifact The name of the artifact to publish to maven centeral
     */
    fun publish(fn: PublishArguments.() -> Unit) {
        var pom = PublishArguments()
        fn(pom)
        project.configureMavenPublish(pom)
    }
}
