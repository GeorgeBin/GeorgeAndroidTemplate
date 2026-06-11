package com.georgebindragon.android.core.service

sealed interface ForegroundServiceState {
    data object Stopped : ForegroundServiceState
    data class Starting(val serviceKey: String) : ForegroundServiceState
    data class Running(val serviceKey: String, val startedAtMillis: Long) : ForegroundServiceState
    data class Stopping(val serviceKey: String) : ForegroundServiceState
    data class Failed(val serviceKey: String, val reason: String) : ForegroundServiceState
}
