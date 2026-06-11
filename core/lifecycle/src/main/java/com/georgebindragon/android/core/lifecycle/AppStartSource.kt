package com.georgebindragon.android.core.lifecycle

sealed interface AppStartSource {
    data object Launcher : AppStartSource
    data object BootCompleted : AppStartSource
    data object Notification : AppStartSource
    data object DeepLink : AppStartSource
    data object Service : AppStartSource
    data class Unknown(val reason: String = "") : AppStartSource
}
