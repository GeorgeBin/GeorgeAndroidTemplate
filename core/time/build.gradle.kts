plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.time"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
}
