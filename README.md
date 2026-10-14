# Lodestone

A simple gradle plugin to configure my gradle projects

## Usage

```kotlin
lodestone {
    // use jvm version 8
    jvm(8)

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

    // add detekt to the `gradle lint` task
    // see `res/detekt.yml` for the config
    detekt()

    // add spotless to the `gradle lint` and `gradle format` tasks
    // currently 120 width kotlin lang style with ktfmt is hardcoded
    spotless()

    // add code coverate support with kover, only needed in the rootProject
    // use task `gradle coverage`
    kover()
}
```
