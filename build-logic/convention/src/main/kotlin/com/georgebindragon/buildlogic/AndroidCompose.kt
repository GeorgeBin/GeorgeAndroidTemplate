package com.georgebindragon.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    extension: ApplicationExtension,
) {
    extension.buildFeatures.compose = true
    configureComposeDependenciesAndCompiler()
}

internal fun Project.configureAndroidCompose(
    extension: LibraryExtension,
) {
    extension.buildFeatures.compose = true
    configureComposeDependenciesAndCompiler()
}

private fun Project.configureComposeDependenciesAndCompiler() {

    dependencies {
        add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
        add("androidTestImplementation", platform(libs.findLibrary("androidx-compose-bom").get()))
        add("implementation", libs.findLibrary("androidx-compose-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-foundation").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        add("implementation", libs.findLibrary("androidx-compose-material3").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
        add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
    }

    extensions.configure(ComposeCompilerGradlePluginExtension::class.java) {
        val projectBuildPath = projectDir.relativeTo(rootDir).invariantSeparatorsPath

        metricsDestination.set(rootProject.layout.buildDirectory.dir("$projectBuildPath/compose-metrics"))
        reportsDestination.set(rootProject.layout.buildDirectory.dir("$projectBuildPath/compose-reports"))
        stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("compose_compiler_config.conf"))
    }
}
