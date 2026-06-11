package com.georgebindragon.android.core.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class SharedTimerManager(
    private val timerSource: TimerSource,
    private val scope: CoroutineScope,
) {
    private val sharedTimers = mutableMapOf<Long, Flow<TimerTick>>()

    fun ticks(intervalMillis: Long): Flow<TimerTick> {
        require(intervalMillis > 0L) { "intervalMillis must be positive" }
        return sharedTimers.getOrPut(intervalMillis) {
            timerSource.ticks(intervalMillis).shareIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 0L, replayExpirationMillis = 0L),
                replay = 0,
            )
        }
    }
}
