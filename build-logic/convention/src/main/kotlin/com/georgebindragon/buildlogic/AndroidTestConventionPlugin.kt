package com.georgebindragon.buildlogic

import com.android.build.api.dsl.TestExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.test")
        if (extensions.findByName("kotlin") == null && !pluginManager.hasPlugin("org.jetbrains.kotlin.android")) {
            pluginManager.apply("org.jetbrains.kotlin.android")
        }
        pluginManager.apply("george.android.lint")

        extensions.configure<TestExtension> {
            configureKotlinAndroid(this)

            defaultConfig {
                targetSdk = 36
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }

        dependencies {
            add("implementation", libs.findLibrary("androidx-junit").get())
            add("implementation", libs.findLibrary("androidx-espresso-core").get())
        }
    }
}
