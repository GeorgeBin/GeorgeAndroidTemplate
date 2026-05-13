plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.settings"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
}
