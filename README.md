# Lodestone

A simple gradle plugin to configure my kotlin gradle projects.

## Setup

Put the following in your `settings.gradle.kts`.
```kotlin
pluginManagement {
    repositories {
        maven("https://maven.freya.cat/snapshots")
    }
}
```

Put the following in your `build.gradle.kts`.
```kotlin
plugins {
    id("cat.freya.lodestone")
}
```

## Usage

```kotlin
lodestone {
    // how to enable java
    java()  // use latest lts jvm version
    java(8) // use jvm 8

    // enable kotlin
    kotlin()        // use latest kotlin release
    kotlin("2.4.0") // use a given kotlin release

    // store information used for processing resources
    // and generating BuildInfo.kt
    buildInfo {
        put("id", rootProject.name)
        put("version", rootProject.version)
        put("name", "Example Project")
        put("author", "KenshinEto")
    }

    // generate a BuildInfo.kt file using the info
    // from buildInfo {}
    generateBuildInfo()

    // process resources, templating information
    // from buildInfo {}
    resources("example.yml", "META-INF/example.toml")

    // if we want to apply the shadow jar configuration just yet.
    // if false, it allows subprojects to extend configuration
    var applyShadow = false

    // configure shadow jar
    shadow(applyShadow) {
        // do we want to shade kotlin stdlib into our project?
        includeProjectName = true

        // relocate dependencies to "${rootProject.group}.deps"
        relocations.add("com.example")
        relocations.add("org.bstats")
    }

    // add support for testing with junit
    // tests are stored in the 'test' directory
    junit()

    // add detekt to the `gradle lint` task
    // see `res/detekt.yml` for the config
    detekt()

    // add spotless to the `gradle lint` and `gradle format` tasks
    // currently 120 width kotlin lang style with ktfmt is hardcoded
    spotless()

    // add code coverate support with kover, only needed in the rootProject
    // use task `gradle coverage`
    kover()

    // publish to maven centeral
    publish {
        artifact = "example"
        group = "cat.freya"
        author = "KenshinEto"
        license = License.GPL3 // GPL2, GPL3, AGPL3, MIT
        gitRepo = "https://github.com/kenshineto/example"

        // uses mavenCenteralUsername and mavenCentralPassword
        mavenCentral()

        // use mavenUsername and mavenPassword
        maven("https://maven.example.com")
        reposilite("https://maven.example.com")

        // uses exampleUsername and examplePassword
        maven {
            name = "example"
            url = "https://maven.example.com"
        }
    }

}
```

## Directories

This plugin changes the default source directories.

| folder | name |
| ------ | ---- |
| Source code | src |
| Resources | res |
| Tests | test |
