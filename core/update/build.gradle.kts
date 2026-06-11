plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.update"
}

dependencies {
    implementation(project(":base:common"))
    implementation(libs.kotlinx.coroutines.core)
}
