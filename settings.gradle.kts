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

rootProject.name = "CNC_Helper"
include(":app")

// 🔥 ЭТА СТРОКА ОБЯЗАТЕЛЬНА для работы alias в корневом build.gradle.kts
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")":app"