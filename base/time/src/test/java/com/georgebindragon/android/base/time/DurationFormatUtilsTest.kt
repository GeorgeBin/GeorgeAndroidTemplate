package com.georgebindragon.android.base.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration

class DurationFormatUtilsTest {
    @Test
    fun compactFormatsDurationBelowOneHourAsMinutesAndSeconds() {
        assertEquals("01:05", DurationFormatUtils.compact(Duration.ofSeconds(65L)))
    }

    @Test
    fun compactFormatsDurationAtLeastOneHourAsHoursMinutesAndSeconds() {
        val duration = Duration.ofHours(2L).plusMinutes(3L).plusSeconds(4L)

        assertEquals("02:03:04", DurationFormatUtils.compact(duration))
    }

    @Test
    fun compactFormatsZeroAndNegativeDurations() {
        assertEquals("00:00", DurationFormatUtils.compact(Duration.ZERO))
        assertEquals("-01:05", DurationFormatUtils.compact(Duration.ofSeconds(-65L)))
    }
}
