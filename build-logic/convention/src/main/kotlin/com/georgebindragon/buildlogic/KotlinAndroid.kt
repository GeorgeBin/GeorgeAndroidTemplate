package com.georgebindragon.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

internal fun Project.configureKotlinAndroid(
    extension: ApplicationExtension,
) {
    extension.compileSdk = 36
    extension.defaultConfig.minSdk = 23
    extension.compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    configureKotlinAndroidCompiler()
}

internal fun Project.configureKotlinAndroid(
    extension: LibraryExtension,
) {
    extension.compileSdk = 36
    extension.defaultConfig.minSdk = 23
    extension.compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    configureKotlinAndroidCompiler()
}

internal fun Project.configureKotlinAndroid(
    extension: TestExtension,
) {
    extension.compileSdk = 36
    extension.defaultConfig.minSdk = 23
    extension.compileOptions.apply {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    configureKotlinAndroidCompiler()
}

private fun Project.configureKotlinAndroidCompiler() {
    extensions.findByType(KotlinAndroidProjectExtension::class.java)?.compilerOptions?.apply {
        jvmTarget.set(JvmTarget.JVM_17)
        allWarningsAsErrors.set(
            providers.gradleProperty("warningsAsErrors")
                .map(String::toBoolean)
                .orElse(false)
        )
    }

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android-desugarJdkLibs").get())
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.findByType(KotlinJvmProjectExtension::class.java)?.compilerOptions?.apply {
        jvmTarget.set(JvmTarget.JVM_17)
        allWarningsAsErrors.set(
            providers.gradleProperty("warningsAsErrors")
                .map(String::toBoolean)
                .orElse(false)
        )
    }
}
