plugins {
    id("george.android.feature")
}

android {
    namespace = "com.georgebindragon.android.feature.home"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.kotlinx.coroutines.test)
}
