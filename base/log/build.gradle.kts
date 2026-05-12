plugins {
    id("george.android.library")
}

android {
    namespace = "com.georgebindragon.android.base.log"
}

dependencies {
    //    implementation(project(":core:common"))
    //    implementation(project(":core:data"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}
