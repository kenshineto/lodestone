import cat.freya.lodestone.publishing.License

plugins {
    `kotlin-dsl`
    alias(libs.plugins.lodestone)
    alias(libs.plugins.vanniktech.publish)
}

group = "cat.freya.lodestone"

version = "0.3-SNAPSHOT"

description = "Gradle plugin for KenshinEto's projects"

val scm = "https://g.freya.cat/lodestone"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

lodestone {
    kotlin(embeddedKotlinVersion)
    detekt()
    spotless()
    publish {
        artifact = "lodestone-gradle-plugin"
        group = "cat.freya"
        author = "KenshinEto"
        license = License.GPL3
        gitRepo = scm
        reposilite("https://maven.freya.cat")
    }
}

dependencies {
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.kover.gradle.plugin)
    implementation(libs.shadow.gradle.plugin) {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation(libs.spotless.gradle.plugin)
    implementation(libs.vanniktech.gradle.plugin)
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$embeddedKotlinVersion")
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
    }
}
