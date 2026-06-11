package com.georgebindragon.android.app

import android.app.Application
import com.georgebindragon.android.base.crash.CrashReporter
import com.georgebindragon.android.base.log.TemplateLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        TemplateLogger.initialize(this)
        CrashReporter.initialize(this)
    }

    companion object {
        lateinit var instance: AppApplication
            private set
    }
}
