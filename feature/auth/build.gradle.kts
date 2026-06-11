plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.auth"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":core:appconfig"))
    implementation(project(":core:auth"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)
}
