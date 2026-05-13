package com.georgebindragon.android.app

import android.app.Application

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppDependencies.init(this)
    }

    companion object {
        lateinit var instance: AppApplication
            private set
    }
}
