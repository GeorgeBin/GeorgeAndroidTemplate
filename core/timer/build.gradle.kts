plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.timer"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
}
