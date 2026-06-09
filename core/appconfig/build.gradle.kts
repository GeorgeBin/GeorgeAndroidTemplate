plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.appconfig"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":core:permission"))
}
