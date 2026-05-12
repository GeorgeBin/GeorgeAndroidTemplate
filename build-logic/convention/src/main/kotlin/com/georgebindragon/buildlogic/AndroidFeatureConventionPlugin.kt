package com.georgebindragon.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("george.android.library")
        pluginManager.apply("george.android.library.compose")
    }
}
