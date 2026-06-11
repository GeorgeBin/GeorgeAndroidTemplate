package com.georgebindragon.android.core.time

import android.os.SystemClock
import java.time.ZoneId

interface SystemTimeProvider {
    fun currentTimeMillis(): Long
    fun elapsedRealtimeMillis(): Long
    fun zoneId(): ZoneId
}

class AndroidSystemTimeProvider : SystemTimeProvider {
    override fun currentTimeMillis(): Long = System.currentTimeMillis()

    override fun elapsedRealtimeMillis(): Long = SystemClock.elapsedRealtime()

    override fun zoneId(): ZoneId = ZoneId.systemDefault()
}

class CalibratedTimeProvider(
    private val systemTimeProvider: SystemTimeProvider,
    initialOffsetMillis: Long = 0L,
) : SystemTimeProvider {
    private var offsetMillis: Long = initialOffsetMillis

    override fun currentTimeMillis(): Long = systemTimeProvider.currentTimeMillis() + offsetMillis

    override fun elapsedRealtimeMillis(): Long = systemTimeProvider.elapsedRealtimeMillis()

    override fun zoneId(): ZoneId = systemTimeProvider.zoneId()

    fun setCalibrationOffset(offsetMillis: Long) {
        this.offsetMillis = offsetMillis
    }
}
