plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.dagger.hilt.android.plugin)
    alias(libs.plugins.kotlin.compose)
}

group = "com.github.m-derakhshan"
version = "1.0.0"


android {
    namespace = "media.hiway.mdkit.translator"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
}



dependencies {
    implementation(libs.squareup.gson)
    implementation(libs.squareup.okhttp3)
    implementation(libs.squareup.retrofit2)

    implementation(libs.androidx.datastore)
    implementation(libs.jetbrains.kotlin.seralization)
    implementation(libs.jetbrains.kotlinx.serialization.json)

    implementation(libs.google.dagger.hilt.android)
    ksp(libs.google.dagger.hilt.android.compiler)

}