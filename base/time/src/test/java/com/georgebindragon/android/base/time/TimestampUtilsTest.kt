package com.georgebindragon.android.base.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TimestampUtilsTest {
    @Test
    fun convertsBetweenMillisSecondsAndInstant() {
        val instant = Instant.parse("2024-01-02T03:04:05.123Z")

        assertEquals(instant, TimestampUtils.millisToInstant(1_704_164_645_123L))
        assertEquals(Instant.parse("2024-01-02T03:04:05Z"), TimestampUtils.secondsToInstant(1_704_164_645L))
        assertEquals(1_704_164_645_123L, TimestampUtils.instantToMillis(instant))
        assertEquals(1_704_164_645L, TimestampUtils.instantToSeconds(instant))
        assertEquals(1_704_164_645L, TimestampUtils.millisToSeconds(1_704_164_645_123L))
        assertEquals(1_704_164_645_000L, TimestampUtils.secondsToMillis(1_704_164_645L))
    }

    @Test
    fun convertsBetweenTimestampAndLocalDateTimeWithZone() {
        val zoneId = ZoneId.of("Asia/Shanghai")
        val localDateTime = LocalDateTime.of(1970, 1, 1, 8, 0, 0)

        assertEquals(localDateTime, TimestampUtils.millisToLocalDateTime(0L, zoneId))
        assertEquals(localDateTime, TimestampUtils.secondsToLocalDateTime(0L, zoneId))
        assertEquals(0L, TimestampUtils.localDateTimeToMillis(localDateTime, zoneId))
        assertEquals(0L, TimestampUtils.localDateTimeToSeconds(localDateTime, zoneId))
    }
}
