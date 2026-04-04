pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://pkgs.dev.azure.com/MicrosoftDeviceSDK/DuoSDK-Public/_packaging/Duo-SDK-Feed/maven/v1")
        }
        maven {
            url = uri("https://pkgs.dev.azure.com/MicrosoftDeviceSDK/7e625948-4386-429e-975b-1c79f4f4ae34/_packaging/DualScreen-SDKs/maven/v1")
            content {
                includeGroup("com.microsoft.device.display")
            }
        }
    }
}

rootProject.name = "AttenCore"
include(":app")
