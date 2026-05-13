plugins {
    id("george.android.library")
}

android {
    namespace = "com.demo.infra.d.template.core.network"
}

dependencies {
    api(libs.kotlinx.serialization.json)
    api(libs.okhttp)
    api(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
}
