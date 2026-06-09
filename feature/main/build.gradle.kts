plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.main"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":core:appconfig"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.navigation.compose)
}
