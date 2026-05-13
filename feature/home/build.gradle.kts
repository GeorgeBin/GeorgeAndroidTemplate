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
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    testImplementation(libs.kotlinx.coroutines.test)
}
