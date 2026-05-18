package com.georgebindragon.android.base.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TimeFormatUtilsTest {
    @Test
    fun formatMillisUsesPatternAndZone() {
        val zoneId = ZoneId.of("Asia/Shanghai")

        val formatted = TimeFormatUtils.formatMillis(
            timestampMillis = 0L,
            pattern = "yyyy-MM-dd HH:mm:ss",
            zoneId = zoneId,
        )

        assertEquals("1970-01-01 08:00:00", formatted)
    }

    @Test
    fun formatSecondsAndInstantAreConsistent() {
        val zoneId = ZoneId.of("UTC")
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")

        assertEquals(
            TimeFormatUtils.format(Instant.ofEpochSecond(1_704_164_645L), formatter, zoneId),
            TimeFormatUtils.formatSeconds(1_704_164_645L, "yyyyMMdd-HHmmss", zoneId),
        )
    }
}
