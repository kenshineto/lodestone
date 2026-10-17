package cat.freya.lodestone.internal

import org.gradle.api.Project
import org.gradle.api.attributes.java.TargetJvmVersion
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named

internal fun Project.configureTests(javaVersion: Int) {
    val mockitoAgent = configurations.create("mockitoAgent")
    val toolchainService = extensions.getByType<JavaToolchainService>()

    tasks.named<Test>("test").configure {
        useJUnitPlatform()
        jvmArgs.add("-javaagent:${mockitoAgent.asPath}")
        javaLauncher.set(
            toolchainService.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(javaVersion))
            },
        )
    }

    // tests always need to use the latest jvm version so that junit can function
    configurations
        .matching { it.name in setOf("testCompileClasspath", "testRuntimeClasspath") }
        .configureEach {
            attributes { attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, javaVersion) }
        }

    dependencies {
        "testImplementation"(Dependencies.kotlinStdlib)
        "testImplementation"(Dependencies.kotlinReflect)
        "testImplementation"(Dependencies.kotlinxCoroutines)
        "testImplementation"(Dependencies.junitJupiterApi)
        "testImplementation"(Dependencies.junitJupiterParams)
        "testRuntimeOnly"(Dependencies.junitJupiterEngine)
        "testRuntimeOnly"(Dependencies.junitJupiterPlatformLauncher)
        "testImplementation"(Dependencies.mockitoCore)
        mockitoAgent(Dependencies.mockitoCore)
    }
}
