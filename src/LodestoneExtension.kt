package cat.freya.lodestone

import cat.freya.lodestone.capability.configureBuildInfo
import cat.freya.lodestone.capability.configureDetekt
import cat.freya.lodestone.capability.configureJava
import cat.freya.lodestone.capability.configureKover
import cat.freya.lodestone.capability.configureResources
import cat.freya.lodestone.capability.configureTasks
import cat.freya.lodestone.capability.configureTests
import cat.freya.lodestone.format.FormatArguments
import cat.freya.lodestone.format.configureSpotless
import cat.freya.lodestone.kotlin.KotlinVersions
import cat.freya.lodestone.kotlin.configureKotlin
import cat.freya.lodestone.publishing.PublishArguments
import cat.freya.lodestone.publishing.configureMavenPublish
import cat.freya.lodestone.shadow.ShadowArguments
import cat.freya.lodestone.shadow.configureShadow
import cat.freya.lodestone.shadow.initalizeShadow
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.provider.MapProperty

abstract class LodestoneExtension @Inject constructor(private val project: Project) {
    abstract val buildInfoValues: MapProperty<String, Any>

    private var hasKotlin: Boolean = false
    private var hasJava: Boolean = false
    private var jvmVersion: Int = Versions.JAVA

    private val shadowArguments = ShadowArguments()
    private val kotlinVersions = KotlinVersions()

    init {
        buildInfoValues.convention(emptyMap())
        project.initalizeShadow()
    }

    /**
     * Configure the project to use java
     *
     * @param jvmVersion The java version to use
     */
    fun java(jvmVersion: Int = Versions.JAVA) {
        if (jvmVersion < 0) {
            error("jvmVersion cannot be negative")
        }

        hasJava = true
        this.jvmVersion = jvmVersion
        project.configureJava(jvmVersion)
        project.configureTasks()
    }

    /**
     * Configure the project to use kotlin. Java is auto configured.
     *
     * @param jvmVersion The java version to use
     * @param kotlinVersion The kotlin version to use
     */
    fun kotlin(kotlinVersion: String = Versions.KOTLIN) {
        if (!hasJava) {
            java(this.jvmVersion)
        }

        hasKotlin = true
        kotlinVersions.set(kotlinVersion)
        project.configureKotlin(this.jvmVersion, kotlinVersions)
    }

    /** Set build info information */
    fun buildInfo(fn: MutableMap<String, Any>.() -> Unit) {
        val map = mutableMapOf<String, Any>()
        fn(map)
        buildInfoValues.convention(map)
    }

    /** Generate a BuildInfo.kt in the root projects group */
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
    fun shadow(apply: Boolean, fn: ShadowArguments.() -> Unit) {
        fn(shadowArguments)
        if (apply) {
            project.configureShadow(shadowArguments, kotlinVersions)
        }
    }

    /** Include spotless formatting into the gradle project */
    fun spotless(fn: FormatArguments.() -> Unit = {}) {
        val args = FormatArguments()
        fn(args)
        project.configureSpotless(args, hasKotlin)
    }

    /** Include detekt linting into the gradle project */
    fun detekt() {
        if (!hasKotlin) error("detekt cannot be used without kotlin applied")
        project.configureDetekt()
    }

    /** Include kover code coverage into the gradle project */
    fun kover() {
        if (!hasKotlin) error("kover cannot be used without kotlin applied")
        project.configureKover()
    }

    /** Setup junit testing framework */
    fun junit(jvmVersion: Int = Versions.JAVA) {
        project.configureTests(jvmVersion)
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
