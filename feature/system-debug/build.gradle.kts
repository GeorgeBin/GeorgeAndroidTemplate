plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.systemdebug"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:system"))
    implementation(project(":core:ui"))
}
