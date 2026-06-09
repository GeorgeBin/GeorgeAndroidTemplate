plugins {
    id("george.android.library.compose")
}

android {
    namespace = "com.georgebindragon.android.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
}
