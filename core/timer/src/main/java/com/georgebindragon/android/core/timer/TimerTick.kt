package com.georgebindragon.android.core.timer

data class TimerTick(
    val intervalMillis: Long,
    val tickCount: Long,
    val elapsedRealtimeMillis: Long,
)
