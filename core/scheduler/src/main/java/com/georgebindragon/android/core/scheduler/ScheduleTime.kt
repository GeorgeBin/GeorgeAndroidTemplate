package com.georgebindragon.android.core.scheduler

sealed interface ScheduleTime {
    data class AtMillis(val triggerAtMillis: Long) : ScheduleTime
    data class AfterDelay(val delayMillis: Long) : ScheduleTime
}
