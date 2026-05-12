package com.georgebindragon.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class RootConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            require(target == rootProject) {
                "george.root must be applied to the root project only."
            }

            tasks.register("georgeDoctor") {
                group = "george"
                description = "Prints a short summary of the George build logic wiring."
                doLast {
                    logger.lifecycle("George build logic is active for ${rootProject.name}.")
                }
            }
        }
    }
}
