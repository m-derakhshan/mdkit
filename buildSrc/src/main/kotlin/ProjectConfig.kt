import org.gradle.api.JavaVersion

object ProjectConfig {
    const val COMPILE_SDK = 36
    const val APPLICATION_ID = "media.hiway.mdkit"
    const val MIN_SDK = 24
    const val TARGET_SDK = 36
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1"
    const val JVM_TARGET = "17"
    val javaSourceCompatibility = JavaVersion.VERSION_17
    val javaTargetCompatibility = JavaVersion.VERSION_17
}