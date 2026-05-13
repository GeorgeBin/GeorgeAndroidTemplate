package com.georgebindragon.android.app

import android.app.Application
import com.georgebindragon.android.app.storage.KVManager

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        KVManager.init(this)
    }

    companion object {
        lateinit var instance: AppApplication
            private set
    }
}
