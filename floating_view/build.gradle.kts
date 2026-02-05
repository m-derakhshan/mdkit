apply {
    from("$rootDir/compose-base-module.gradle")
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.serialization.plugin)
}

android {
    namespace = FloatingView.NAMESPACE
    publishing {
        singleVariant("release")
    }
}
