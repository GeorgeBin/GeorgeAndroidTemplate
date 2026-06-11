plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.lifecycle"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
