package com.georgebindragon.android.core.boot

import android.content.Context
import android.content.Intent

interface BootStartHandler {
    fun onBootCompleted(context: Context, intent: Intent)
}

object BootStartHandlerRegistry {
    @Volatile
    var handler: BootStartHandler? = null
}
