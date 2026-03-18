// Dependency Resolution Management Configuration

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SignatureMaker"
include(":app")
include(":skizo-kotlin-core")
include(":skizo-android-core")
