package com.georgebindragon.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("george.android.application")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        val extension = extensions.getByType(ApplicationExtension::class.java)
        configureAndroidCompose(extension)
    }
}
