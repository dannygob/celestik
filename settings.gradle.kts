// Configure plugin management for Gradle
pluginManagement {
    repositories {
        google {
            content {
                // Restrict to Android and Google-related plugin groups
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()       // Standard Maven repository
        gradlePluginPortal() // Gradle plugins repository
    }
}

// Configure dependency resolution for all modules
dependencyResolutionManagement {
    // Fail if a module tries to declare its own repositories
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()        // Android dependencies
        mavenCentral()  // General dependencies

        // JitPack repository for libraries hosted on GitHub
        maven {
            url = uri("https://jitpack.io")
        }

        // Repository required for AprilTag library
        maven {
            url = uri("https://frcmaven.wpi.edu/artifactory/release/")
        }
    }
}

// Define the root project name
rootProject.name = "celestik"

// ✅ Only include the app module (OpenCV module removed)
include(":app")