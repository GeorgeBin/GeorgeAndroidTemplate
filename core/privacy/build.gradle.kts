plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.privacy"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":core:datastore"))
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
}
