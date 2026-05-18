plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.base.crash"
}

dependencies {
    implementation(project(":base:log"))
}
