package com.georgebindragon.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.TestExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.findByType(ApplicationExtension::class.java)?.lint?.apply {
                abortOnError = true
                checkDependencies = true
                warningsAsErrors = providers.gradleProperty("warningsAsErrors")
                    .map(String::toBoolean)
                    .orElse(false)
                    .get()
            }
            extensions.findByType(LibraryExtension::class.java)?.lint?.apply {
                abortOnError = true
                checkDependencies = true
                warningsAsErrors = providers.gradleProperty("warningsAsErrors")
                    .map(String::toBoolean)
                    .orElse(false)
                    .get()
            }
            extensions.findByType(TestExtension::class.java)?.lint?.apply {
                abortOnError = true
                warningsAsErrors = providers.gradleProperty("warningsAsErrors")
                    .map(String::toBoolean)
                    .orElse(false)
                    .get()
            }
        }
    }
}
