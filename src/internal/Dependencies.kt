package cat.freya.lodestone.internal

import org.gradle.api.Project

internal object Versions {
    const val JAVA = 25
    const val JUNIT = "6.+"
    const val KOTLIN = "2.4.+"
    const val KOTLINX_COROUTINES = "1.+"
    const val KOTLINX_KOVER = "0.9.+"
    const val KTFMT = "0.65"
    const val MOCKITO = "5.+"
}

internal object Dependencies {
    const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}"
    const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}"
    const val junitJupiterParams = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT}"
    const val junitJupiterPlatformLauncher = "org.junit.platform:junit-platform-launcher:${Versions.JUNIT}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    const val kotlinxCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLINX_COROUTINES}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.MOCKITO}"
}
