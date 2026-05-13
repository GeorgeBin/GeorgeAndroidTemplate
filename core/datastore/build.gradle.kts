plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.core.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.kotlinx.coroutines.test)
}
