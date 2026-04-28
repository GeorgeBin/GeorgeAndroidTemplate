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

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven("https://jitpack.io")
        maven("https://repo1.maven.org/maven2/")

        // maven mirror

        // 阿里云 https://developer.aliyun.com/mvn/guide
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")

        // 华为 https://mirrors.huaweicloud.com
        maven("https://repo.huaweicloud.com/repository/maven/")

        // 网易 http://mirrors.163.com/
        // maven { url "https://mirrors.163.com/maven/repository/maven-public/" }

        // 腾讯 https://mirrors.cloud.tencent.com/
        // maven { url "http://mirrors.tencent.com/nexus/repository/maven-public/" }
    }
}

rootProject.name = "GeorgeAndroidTemplate"
include(":app")
include(":corelib")
include(":demo")
