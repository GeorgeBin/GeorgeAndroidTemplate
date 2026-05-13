plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.settings"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:settings"))
}
