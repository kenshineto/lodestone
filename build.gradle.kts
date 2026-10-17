import cat.freya.lodestone.License

plugins {
    `kotlin-dsl`
    alias(libs.plugins.lodestone)
    alias(libs.plugins.vanniktech.publish)
}

group = "cat.freya.lodestone"

version = "0.1-SNAPSHOT"

description = "Gradle plugin for KenshinEto's projects"

val scm = "https://g.freya.cat/lodestone"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

lodestone {
    jvm()
    detekt()
    spotless()
    publish {
        group = "cat.freya"
        artifact = "lodestone-gradle-plugin"
        author = "KenshinEto"
        url = scm
        license = License.GPL3
    }
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

gradlePlugin {
    website.set(scm)
    vcsUrl.set(scm)

    plugins {
        create("lodestone") {
            id = rootProject.group.toString()
            implementationClass = "${rootProject.group}.LodestonePlugin"
            displayName = rootProject.name
            description = rootProject.description
        }

        create("lodestone-minimal") {
            id = rootProject.group.toString() + "-minimal"
            implementationClass = "${rootProject.group}.LodestonePluginMinimal"
            displayName = rootProject.name
            description = rootProject.description
        }
    }
}
