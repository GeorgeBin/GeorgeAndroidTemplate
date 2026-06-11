package com.georgebindragon.android.core.scheduler

data class ScheduleRequest(
    val id: String,
    val time: ScheduleTime,
    val exact: Boolean = true,
    val allowWhileIdle: Boolean = true,
)

data class RepeatingScheduleRequest(
    val id: String,
    val firstTime: ScheduleTime,
    val intervalMillis: Long,
)
