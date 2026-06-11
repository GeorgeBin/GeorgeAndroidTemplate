package com.georgebindragon.android.core.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmManagerScheduler(
    private val context: Context,
    private val alarmManager: AlarmManager =
        context.getSystemService(AlarmManager::class.java),
    private val action: String = DEFAULT_ACTION,
) : AppScheduler {
    override fun schedule(request: ScheduleRequest) {
        val triggerAtMillis = request.time.toTriggerAtMillis()
        val operation = request.toPendingIntent()
        when {
            request.exact && request.allowWhileIdle && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, operation)
            }

            request.exact -> alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, operation)

            else -> alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, operation)
        }
    }

    override fun scheduleRepeating(request: RepeatingScheduleRequest) {
        require(request.intervalMillis > 0L) { "intervalMillis must be positive" }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            request.firstTime.toTriggerAtMillis(),
            request.intervalMillis,
            request.toPendingIntent(),
        )
    }

    override fun cancel(id: String) {
        alarmManager.cancel(pendingIntent(id))
    }

    private fun ScheduleTime.toTriggerAtMillis(): Long = when (this) {
        is ScheduleTime.AtMillis -> triggerAtMillis
        is ScheduleTime.AfterDelay -> System.currentTimeMillis() + delayMillis
    }

    private fun ScheduleRequest.toPendingIntent(): PendingIntent = pendingIntent(id)

    private fun RepeatingScheduleRequest.toPendingIntent(): PendingIntent = pendingIntent(id)

    private fun pendingIntent(id: String): PendingIntent {
        val intent = Intent(action).setPackage(context.packageName).putExtra(EXTRA_ID, id)
        return PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private companion object {
        const val DEFAULT_ACTION = "com.georgebindragon.android.core.scheduler.ALARM"
        const val EXTRA_ID = "schedule_id"
    }
}
