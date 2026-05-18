package com.georgebindragon.android.base.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class CurrentTimeUtilsTest {
    @Test
    fun currentTimeUsesInjectedClock() {
        val instant = Instant.parse("2024-01-02T03:04:05.123Z")
        val clock = Clock.fixed(instant, ZoneOffset.UTC)

        assertEquals(1_704_164_645_123L, CurrentTimeUtils.nowMillis(clock))
        assertEquals(1_704_164_645L, CurrentTimeUtils.nowSeconds(clock))
        assertEquals(instant, CurrentTimeUtils.nowInstant(clock))
    }
}
