plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.permission"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":core:appconfig"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:permission"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.core)
}
