package cat.freya.lodestone.format

import com.diffplug.gradle.spotless.BaseKotlinExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.kotlin.KtfmtStep
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.fileTree

internal fun Project.configureSpotless(args: FormatArguments, applyKotlin: Boolean) {
    pluginManager.apply("com.diffplug.spotless")

    val buildDir =
        fileTree(layout.buildDirectory) {
            include("**/*")
        }

    val configureKtfmt: BaseKotlinExtension.() -> Unit = {
        targetExclude(buildDir)
        ktfmt(args.ktfmtVersion).kotlinlangStyle().configure {
            it.setMaxWidth(args.lineWidth)
            it.setBlockIndent(args.tabWidth)
            it.setContinuationIndent(args.tabWidth)
            it.setTrailingCommaManagementStrategy(KtfmtStep.TrailingCommaManagementStrategy.ONLY_ADD)
            it.setRemoveUnusedImports(true)
        }
    }

    project.extensions.configure<SpotlessExtension> {
        java {
            targetExclude(buildDir)
            val props = buildList {
                add("org.eclipse.jdt.core.formatter.lineSplit=${args.lineWidth}")
                add("org.eclipse.jdt.core.formatter.comment.line_length=${args.lineWidth}")
                add("org.eclipse.jdt.core.formatter.tabulation.size=${args.tabWidth}")
                add("org.eclipse.jdt.core.formatter.indentation.size=${args.tabWidth}")
                add("org.eclipse.jdt.core.formatter.tabulation.char=${if (args.expandTabs) "space" else "tab"}")
            }
            eclipse().configProperties(*props.toTypedArray())
        }
        if (applyKotlin) {
            kotlin(configureKtfmt)
            kotlinGradle(configureKtfmt)
        }
        yaml {
            target("**/*.yml")
            targetExclude(buildDir)
            trimTrailingWhitespace()
            endWithNewline()
        }
        json {
            target("**/*.json")
            targetExclude(buildDir)
            simple().indentWithSpaces(args.tabWidth)
            trimTrailingWhitespace()
            endWithNewline()
        }
        freshmark {
            target("**/*.md")
            targetExclude(buildDir)
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
