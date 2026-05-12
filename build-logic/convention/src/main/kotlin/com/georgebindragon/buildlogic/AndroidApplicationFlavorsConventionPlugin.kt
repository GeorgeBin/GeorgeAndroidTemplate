package com.georgebindragon.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationFlavorsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("george.android.application")

        extensions.configure<ApplicationExtension> {
            flavorDimensions += "content"

            productFlavors {
                maybeCreate("demo").apply {
                    dimension = "content"
                    applicationIdSuffix = ".demo"
                    versionNameSuffix = "-demo"
                }
                maybeCreate("prod").apply {
                    dimension = "content"
                }
            }
        }
    }
}
