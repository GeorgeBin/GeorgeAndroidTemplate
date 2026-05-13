plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.settings"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
