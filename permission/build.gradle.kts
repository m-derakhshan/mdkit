apply {
    from("$rootDir/compose-base-module.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization.plugin)
}

android {
    namespace = Permission.NAMESPACE
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.jetbrains.kotlinx.serialization.json)
}
