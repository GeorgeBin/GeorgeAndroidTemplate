package com.georgebindragon.android.core.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DefaultAppTimeRepository(
    private val timeProvider: SystemTimeProvider,
    scope: CoroutineScope,
    private val tickIntervalMillis: Long = DEFAULT_TICK_INTERVAL_MILLIS,
) : AppTimeRepository {
    private val mutableTimeState = MutableStateFlow(AppTimeState.from(timeProvider))
    override val timeState: StateFlow<AppTimeState> = mutableTimeState.asStateFlow()

    init {
        scope.launch {
            while (isActive) {
                mutableTimeState.value = AppTimeState.from(timeProvider)
                delay(tickIntervalMillis)
            }
        }
    }

    override fun currentState(): AppTimeState = AppTimeState.from(timeProvider)

    override suspend fun calibrate(strategy: TimeCalibrationStrategy): TimeCalibrationResult {
        return when (strategy) {
            TimeCalibrationStrategy.System -> {
                if (timeProvider is CalibratedTimeProvider) {
                    timeProvider.setCalibrationOffset(0L)
                }
                mutableTimeState.value = AppTimeState.from(timeProvider)
                TimeCalibrationResult(
                    strategy = strategy,
                    calibrated = true,
                    source = SYSTEM_SOURCE,
                    message = "Using system time.",
                )
            }

            TimeCalibrationStrategy.Ntp,
            TimeCalibrationStrategy.Http,
            is TimeCalibrationStrategy.Custom -> TimeCalibrationResult(
                strategy = strategy,
                calibrated = false,
                message = "Calibration strategy is not implemented yet.",
            )
        }
    }

    private companion object {
        const val DEFAULT_TICK_INTERVAL_MILLIS = 1_000L
        const val SYSTEM_SOURCE = "system"
    }
}
