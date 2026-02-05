apply {
    from("$rootDir/compose-base-module.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.serialization.plugin)
}

android {
    namespace = Translator.NAMESPACE
}

dependencies {
    implementation(libs.squareup.gson)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.retrofit2)

    implementation(libs.androidx.datastore)
    implementation(libs.jetbrains.kotlinx.serialization.json)
}
