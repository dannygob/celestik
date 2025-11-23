plugins {
    // Android and Kotlin plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)

    // Kotlin annotation processing and parcelize support
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

        // ✅ Native build configuration with CMake
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++11"
            }
        }

        // ✅ ABI filters for native libraries
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a")
        }
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

    // ✅ Java compatibility settings
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // ✅ Modern Kotlin compiler options (replaces deprecated kotlinOptions)
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
        }
    }

    // ✅ Enable Jetpack Compose
    buildFeatures {
        compose = true
    }

    // ✅ Link with CMake (only needed if you add native code later)
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    // ✅ Include precompiled .so libraries from jniLibs
    sourceSets["main"].jniLibs.srcDirs("src/main/jniLibs")
}

// ✅ Kotlin toolchain configuration
kotlin {
    jvmToolchain(21)
}

dependencies {
    // CORE / ANDROIDX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.runtime)

    // JETPACK COMPOSE
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.ui.text)
    implementation(libs.protolite.well.known.types)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ROOM / DATABASE
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.room.external.antlr)

    // NAVIGATION
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.android)

    // HILT / DEPENDENCY INJECTION
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    // CAMERA / MEDIA
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)

    // FIREBASE / GOOGLE
    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.transportation.consumer)
    implementation(libs.gson)
    implementation(libs.litert)

    // UTILITIES
    implementation(libs.shimmer)
    implementation(libs.poi.ooxml)
    implementation(libs.apriltag.java)

    // TESTING
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    implementation(libs.kernel)
    implementation(libs.layout)
    implementation(libs.tensorflow.lite)

    // ✅ OpenCV Java bindings (JAR only, no module reference)
    implementation(files("libs/opencv-4120.jar"))
}