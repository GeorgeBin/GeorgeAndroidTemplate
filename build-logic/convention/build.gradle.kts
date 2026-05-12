import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.georgebindragon.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.compose.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("georgeAndroidApplication") {
            id = "george.android.application"
            implementationClass = "com.georgebindragon.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("georgeAndroidLibrary") {
            id = "george.android.library"
            implementationClass = "com.georgebindragon.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("georgeAndroidApplicationCompose") {
            id = "george.android.application.compose"
            implementationClass = "com.georgebindragon.buildlogic.AndroidApplicationComposeConventionPlugin"
        }
        register("georgeAndroidLibraryCompose") {
            id = "george.android.library.compose"
            implementationClass = "com.georgebindragon.buildlogic.AndroidLibraryComposeConventionPlugin"
        }
        register("georgeAndroidFeature") {
            id = "george.android.feature"
            implementationClass = "com.georgebindragon.buildlogic.AndroidFeatureConventionPlugin"
        }
        register("georgeAndroidTest") {
            id = "george.android.test"
            implementationClass = "com.georgebindragon.buildlogic.AndroidTestConventionPlugin"
        }
        register("georgeAndroidHilt") {
            id = "george.android.hilt"
            implementationClass = "com.georgebindragon.buildlogic.HiltConventionPlugin"
        }
        register("georgeAndroidRoom") {
            id = "george.android.room"
            implementationClass = "com.georgebindragon.buildlogic.AndroidRoomConventionPlugin"
        }
        register("georgeAndroidLint") {
            id = "george.android.lint"
            implementationClass = "com.georgebindragon.buildlogic.AndroidLintConventionPlugin"
        }
        register("georgeAndroidApplicationFlavors") {
            id = "george.android.application.flavors"
            implementationClass = "com.georgebindragon.buildlogic.AndroidApplicationFlavorsConventionPlugin"
        }
        register("georgeJvmLibrary") {
            id = "george.jvm.library"
            implementationClass = "com.georgebindragon.buildlogic.JvmLibraryConventionPlugin"
        }
        register("georgeRoot") {
            id = "george.root"
            implementationClass = "com.georgebindragon.buildlogic.RootConventionPlugin"
        }
    }
}
