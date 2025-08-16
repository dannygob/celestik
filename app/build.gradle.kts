plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.celestik"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.celestik"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Room
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)

    // Otros
    implementation(libs.litert)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose.jvmstubs)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.gson)
    implementation(libs.poi.ooxml)
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("androidx.navigation:navigation-compose:2.7.0")

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // AprilTag
    implementation("edu.wpi.first.apriltag:apriltag-java:2025.3.1")
    // implementation(project(":opencv")) // <- solo si lo necesitas en el futuro

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
