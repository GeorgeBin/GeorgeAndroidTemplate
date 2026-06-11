package com.georgebindragon.android.core.time

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class AppTimeState(
    val systemTimeMillis: Long,
    val elapsedRealtimeMillis: Long,
    val zoneId: ZoneId,
    val date: LocalDate,
    val hour: Int,
    val minute: Int,
    val second: Int,
) {
    companion object {
        fun from(provider: SystemTimeProvider): AppTimeState {
            val systemTimeMillis = provider.currentTimeMillis()
            val zoneId = provider.zoneId()
            val dateTime = Instant.ofEpochMilli(systemTimeMillis).atZone(zoneId)
            return AppTimeState(
                systemTimeMillis = systemTimeMillis,
                elapsedRealtimeMillis = provider.elapsedRealtimeMillis(),
                zoneId = zoneId,
                date = dateTime.toLocalDate(),
                hour = dateTime.hour,
                minute = dateTime.minute,
                second = dateTime.second,
            )
        }
    }
}
