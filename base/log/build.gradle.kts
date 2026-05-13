plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.base.log"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
