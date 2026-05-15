plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.settings"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:input"))
    implementation(project(":core:locale"))
    implementation(project(":core:settings"))
    implementation(project(":core:ui"))
}
