package com.georgebindragon.android.core.scheduler

interface AppScheduler {
    fun schedule(request: ScheduleRequest)
    fun scheduleRepeating(request: RepeatingScheduleRequest)
    fun cancel(id: String)
}
