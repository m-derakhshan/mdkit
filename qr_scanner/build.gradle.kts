apply {
    from("$rootDir/compose-base-module.gradle")
}

plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = QRScanner.NAMESPACE
}

//-----------------camera x------------------------//
dependencies {
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.androidx.camera.compose)
    implementation(libs.google.mlkit.barcode)
    implementation(libs.androidx.camera.camera2)
}