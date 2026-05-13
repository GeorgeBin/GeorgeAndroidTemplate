plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.storage"
}

dependencies {
    implementation(project(":base:io"))
}
