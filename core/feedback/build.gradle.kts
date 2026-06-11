plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.feedback"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
