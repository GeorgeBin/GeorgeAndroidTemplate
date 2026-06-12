plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.message"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.navigation.compose)
}
