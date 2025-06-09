// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}



buildscript {
    dependencies {
        classpath (libs.jetbrains.kotlin.seralization)
        classpath(libs.google.dagger.hilt.android.gradle.plugin)
    }
}
