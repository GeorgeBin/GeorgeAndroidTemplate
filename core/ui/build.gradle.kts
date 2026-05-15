plugins {
    id("george.android.library.compose")
}

android {
    namespace = "com.georgebindragon.android.core.ui"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:input"))
}

