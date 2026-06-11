package com.georgebindragon.android.core.time

import kotlinx.coroutines.flow.StateFlow

interface TimeChangeObserver {
    val timeState: StateFlow<AppTimeState>
}

interface AppTimeRepository : TimeChangeObserver {
    fun currentState(): AppTimeState
    suspend fun calibrate(strategy: TimeCalibrationStrategy): TimeCalibrationResult
}
