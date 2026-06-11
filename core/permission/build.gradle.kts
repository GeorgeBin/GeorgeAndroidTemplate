plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.permission"
}

dependencies {
    implementation(project(":base:common"))
    implementation(project(":core:datastore"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.kotlinx.coroutines.test)
}
