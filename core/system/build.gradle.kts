plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.system"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":base:shell"))

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
