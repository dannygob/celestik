// Top-level Gradle build file
// This file configures plugins that can be applied in sub-projects/modules.
// ✅ No references to the removed OpenCV module, only shared plugin definitions.

plugins {
    // Android application plugin (applied in :app module)
    alias(libs.plugins.android.application) apply false

    // Kotlin Android plugin (applied in :app module)
    alias(libs.plugins.kotlin.android) apply false

    // Kotlin Compose plugin (applied in :app module)
    alias(libs.plugins.kotlin.compose) apply false

    // Dagger Hilt plugin for dependency injection (applied in :app module)
    alias(libs.plugins.dagger.hilt.android) apply false
}