plugins {
    id("george.android.library.compose")
}

android {
    namespace = "com.georgebindragon.android.core.designsystem"
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
}
