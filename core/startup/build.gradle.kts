plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.startup"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":core:appconfig"))
    implementation(project(":core:permission"))
    implementation(project(":core:privacy"))

    testImplementation(libs.kotlinx.coroutines.test)
}
