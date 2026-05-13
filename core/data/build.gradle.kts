plugins {
    id("george.android.library")
}

android {
    namespace = "com.demo.infra.d.template.core.data"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(libs.kotlinx.coroutines.core)
}
