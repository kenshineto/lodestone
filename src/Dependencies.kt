package cat.freya.lodestone

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
    const val JUNIT_JUPITER_API = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}"
    const val JUNIT_JUPITER_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}"
    const val JUNIT_JUPITER_PARAMS = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT}"
    const val JUNIT_JUPITER_PLATFORM_LAUNCHER = "org.junit.platform:junit-platform-launcher:${Versions.JUNIT}"
    const val KOTLINX_COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLINX_COROUTINES}"
    const val MOCKITO_CORE = "org.mockito:mockito-core:${Versions.MOCKITO}"

    fun kotlinReflect(kotlinVersion: String) = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"

    fun kotlinStdlib(kotlinVersion: String) = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
}
