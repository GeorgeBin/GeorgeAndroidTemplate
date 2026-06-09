plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.startup"
}

dependencies {
    implementation(project(":core:appconfig"))

    testImplementation(libs.kotlinx.coroutines.test)
}
