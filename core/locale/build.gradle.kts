plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.locale"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
}

