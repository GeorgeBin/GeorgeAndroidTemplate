package com.georgebindragon.android.core.timer

import android.os.SystemClock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun interface TimerSource {
    fun ticks(intervalMillis: Long): Flow<TimerTick>
}

class CoroutineTimerSource(
    private val elapsedRealtimeMillis: () -> Long = SystemClock::elapsedRealtime,
) : TimerSource {
    override fun ticks(intervalMillis: Long): Flow<TimerTick> = flow {
        require(intervalMillis > 0L) { "intervalMillis must be positive" }
        var tickCount = 0L
        while (true) {
            delay(intervalMillis)
            tickCount += 1
            emit(
                TimerTick(
                    intervalMillis = intervalMillis,
                    tickCount = tickCount,
                    elapsedRealtimeMillis = elapsedRealtimeMillis(),
                ),
            )
        }
    }
}
