plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.privacy"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":core:appconfig"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:privacy"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.navigation.compose)
}
