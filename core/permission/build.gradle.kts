plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.permission"
}

dependencies {
    implementation(project(":base:common"))
}
