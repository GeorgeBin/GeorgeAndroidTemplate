package com.georgebindragon.android.core.lifecycle

sealed interface AppForegroundState {
    data object Foreground : AppForegroundState
    data object Background : AppForegroundState
}
