import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("george.android.application")
    id("george.android.application.compose")
    id("george.android.hilt")
}

// apk 打包：类型 + 文件命名 + 存储路径
val apkExportBuildType = providers.gradleProperty("apkExportBuildType").orElse("release")!!
val apkExportDir = providers.gradleProperty("apkExportDir").orElse("${projectDir}/build/apk")!!
val apkFileNameFormat = providers.gradleProperty("apkFileNameFormat")
    .orElse($$"${appName}-v${versionName}-${buildType}.apk")!!

// apk 打包：签名
val signingStoreFilePath = providers.gradleProperty("signing.storeFile")
    .orElse("${rootDir}/signkey/systemkey.jks")!!
val signingStoreFile = file(signingStoreFilePath.get())
val hasReleaseSigning = signingStoreFile.exists()
val signingKeyAlias = providers.gradleProperty("signing.keyAlias").orElse("zyzl")!!
val signingKeyPassword = providers.gradleProperty("signing.keyPassword").orElse("androidsystem")!!
val signingStorePassword = providers.gradleProperty("signing.storePassword").orElse("androidsystem")!!

android {
    namespace = "com.georgebindragon.android.app"

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        ndk {
            // noinspection ChromeOsAbiSupport
            abiFilters += setOf("armeabi-v7a", "arm64-v8a")
        }

        applicationId = "z.demo.xxx" //todo：包名

        versionCode = 1_01
        versionName = "1.01" //todo：版本号

        // 更新记录
    }

    signingConfigs {
        create("release") {
            keyAlias = signingKeyAlias.get()
            keyPassword = signingKeyPassword.get()
            storeFile = signingStoreFile
            storePassword = signingStorePassword.get()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    implementation(project(":base:crash"))
    implementation(project(":base:common"))
    implementation(project(":base:log"))
    implementation(project(":core:appconfig"))
    implementation(project(":core:auth"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:input"))
    implementation(project(":core:locale"))
    implementation(project(":core:navigation"))
    implementation(project(":core:permission"))
    implementation(project(":core:privacy"))
    implementation(project(":core:settings"))
    implementation(project(":core:startup"))
    implementation(project(":core:ui"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:home"))
    implementation(project(":feature:main"))
    implementation(project(":feature:message"))
    implementation(project(":feature:permission"))
    implementation(project(":feature:privacy"))
    implementation(project(":feature:settings"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.process.phoenix)
}

tasks.register<Copy>("exportApk") {
    group = "george"
    description = "Builds APK and copies it to configurable directory (apkExportDir)."

    val buildType = apkExportBuildType.get().trim().lowercase()
    require(buildType == "release" || buildType == "debug") {
        "apkExportBuildType must be 'release' or 'debug', but was '$buildType'"
    }
    val assembleTask = "assemble" + buildType.replaceFirstChar { it.uppercase() }
    dependsOn(assembleTask)

    val appName = "AndroidTemplate"
    val appId = android.defaultConfig.applicationId ?: "unknown.package"
    val versionName = android.defaultConfig.versionName ?: "0.0.0"
    val versionCode = android.defaultConfig.versionCode ?: 0
    val buildTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
    val resolvedApkName = apkFileNameFormat.get()
        .replace($$"${appName}", appName)
        .replace($$"${packageName}", appId)
        .replace($$"${buildType}", buildType)
        .replace($$"${versionName}", versionName)
        .replace($$"${versionCode}", versionCode.toString())
        .replace($$"${buildTime}", buildTime)

    from(layout.buildDirectory.dir("outputs/apk/$buildType")) {
        include("*.apk")
    }
    into(file(apkExportDir.get()))
    rename { resolvedApkName }

    doFirst {
        if (buildType == "release" && !hasReleaseSigning) {
            logger.error(
                "Release signing keystore not found: ${signingStoreFile.absolutePath}. " +
                        "Release APK will use the default signing behavior."
            )
        }
        logger.lifecycle("exportApk: buildType=$buildType")
        logger.lifecycle("exportApk: outputDir=${file(apkExportDir.get()).absolutePath}")
        logger.lifecycle("exportApk: fileName=$resolvedApkName")
    }
}
