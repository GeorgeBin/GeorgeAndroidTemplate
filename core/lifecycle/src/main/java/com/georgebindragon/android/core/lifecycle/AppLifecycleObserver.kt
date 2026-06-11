package com.georgebindragon.android.core.lifecycle

import kotlinx.coroutines.flow.StateFlow

interface AppLifecycleObserver {
    val foregroundState: StateFlow<AppForegroundState>
    val lastStartSource: StateFlow<AppStartSource>

    fun onAppStarted(source: AppStartSource)
    fun onForegroundStateChanged(state: AppForegroundState)
}
