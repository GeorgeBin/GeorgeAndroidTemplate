pluginManagement {
    includeBuild("build-logic")

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
        // 镜像
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/central")
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application", "com.android.library" -> {
                    useModule("com.android.tools.build:gradle:${requested.version}")
                }
                "org.jetbrains.kotlin.android",
                "org.jetbrains.kotlin.jvm" -> {
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                }
                "org.jetbrains.kotlin.plugin.compose" -> {
                    useModule("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:${requested.version}")
                }
                "org.jetbrains.kotlin.plugin.serialization" -> {
                    useModule("org.jetbrains.kotlin.plugin.serialization:org.jetbrains.kotlin.plugin.serialization.gradle.plugin:${requested.version}")
                }
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 镜像
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/central")

        google()
        mavenCentral()
    }
}


include(
    ":app",
    ":base:common",
    ":base:crash",
    ":base:io",
    ":base:log",
    ":base:media",
    ":base:network-tool",
    ":base:security",
    ":base:shell",
    ":base:time",
    ":core:adaptive",
    ":core:auth",
    ":core:boot",
    ":core:data",
    ":core:database",
    ":core:datastore",
    ":core:designsystem",
    ":core:feedback",
    ":core:input",
    ":core:lifecycle",
    ":core:locale",
    ":core:model",
    ":core:appconfig",
    ":core:navigation",
    ":core:network",
    ":core:notification",
    ":core:permission",
    ":core:privacy",
    ":core:service",
    ":core:settings",
    ":core:startup",
    ":core:storage",
    ":core:scheduler",
    ":core:time",
    ":core:timer",
    ":core:update",
    ":core:ui",
    ":feature:home",
    ":feature:auth",
    ":feature:main",
    ":feature:permission",
    ":feature:privacy",
    ":feature:settings",
)


rootProject.name = "Android：Template"  //todo：项目别名
