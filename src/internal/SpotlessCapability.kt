package cat.freya.lodestone.internal

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

internal fun Project.configureSpotless() {
    pluginManager.apply("com.diffplug.spotless")

    project.extensions.configure<SpotlessExtension> {
        kotlin {
            ktfmt(Versions.KTFMT).kotlinlangStyle().configure {
                it.setMaxWidth(120)
                it.setBlockIndent(4)
                it.setContinuationIndent(4)
                it.setRemoveUnusedImports(true)
            }
        }
        kotlinGradle {
            ktfmt(Versions.KTFMT).kotlinlangStyle().configure {
                it.setMaxWidth(120)
                it.setBlockIndent(4)
                it.setContinuationIndent(4)
                it.setRemoveUnusedImports(true)
            }
        }
        yaml {
            target("**/*.yml")
            jackson()
                .yamlFeature("WRITE_DOC_START_MARKER", false)
                .yamlFeature("INDENT_ARRAYS_WITH_INDICATOR", true)
                .yamlFeature("LITERAL_BLOCK_STYLE", true)
        }
        json {
            target("**/*.json")
            simple().indentWithSpaces(4)
        }
        freshmark {
            target("**/*.md")
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
