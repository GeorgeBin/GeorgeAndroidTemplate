plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.network"
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.serialization.json)
    api(libs.okhttp)
    api(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
}
