plugins {
    id("george.android.library.compose")
}

android {
    namespace = "com.georgebindragon.android.core.adaptive"
}

dependencies {
    implementation(libs.androidx.compose.material3.adaptive)
}
