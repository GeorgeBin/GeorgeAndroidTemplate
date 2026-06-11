package com.georgebindragon.android.core.time

sealed interface TimeCalibrationStrategy {
    data object System : TimeCalibrationStrategy
    data object Ntp : TimeCalibrationStrategy
    data object Http : TimeCalibrationStrategy
    data class Custom(val source: String) : TimeCalibrationStrategy
}

data class TimeCalibrationResult(
    val strategy: TimeCalibrationStrategy,
    val calibrated: Boolean,
    val offsetMillis: Long = 0L,
    val source: String = "",
    val message: String = "",
)
