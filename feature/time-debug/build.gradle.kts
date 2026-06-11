plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.timedebug"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:time"))
    implementation(project(":core:ui"))
}
