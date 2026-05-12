package com.demo.infra.d.template

import android.app.Application
import com.demo.infra.d.template.storage.KVManager

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
