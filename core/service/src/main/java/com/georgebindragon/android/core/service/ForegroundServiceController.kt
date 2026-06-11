package com.georgebindragon.android.core.service

import kotlinx.coroutines.flow.StateFlow

interface ForegroundServiceController {
    val state: StateFlow<ForegroundServiceState>

    fun start(serviceKey: String)
    fun stop(serviceKey: String)
}
