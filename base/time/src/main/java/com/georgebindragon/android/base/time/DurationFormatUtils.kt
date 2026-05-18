package com.georgebindragon.android.base.time

import java.time.Duration

object DurationFormatUtils {
    fun compact(duration: Duration): String {
        val negative = duration.isNegative
        val absoluteSeconds = duration.abs().seconds
        val hours = absoluteSeconds / SECONDS_PER_HOUR
        val minutes = (absoluteSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE
        val seconds = absoluteSeconds % SECONDS_PER_MINUTE
        val formatted = if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
        return if (negative) "-$formatted" else formatted
    }

    private const val SECONDS_PER_MINUTE = 60L
    private const val SECONDS_PER_HOUR = 60L * SECONDS_PER_MINUTE
}
