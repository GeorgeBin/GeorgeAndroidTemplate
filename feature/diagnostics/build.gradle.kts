plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.diagnostics"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":base:network-tool"))
    implementation(project(":base:shell"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:lifecycle"))
    implementation(project(":core:permission"))
    implementation(project(":core:ui"))
}
