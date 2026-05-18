package com.georgebindragon.android.base.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

class TimeDiffUtilsTest {
    @Test
    fun betweenMillisSupportsPositiveNegativeAndZeroDurations() {
        assertEquals(Duration.ofMillis(250L), TimeDiffUtils.betweenMillis(1_000L, 1_250L))
        assertEquals(Duration.ofMillis(-250L), TimeDiffUtils.betweenMillis(1_250L, 1_000L))
        assertEquals(Duration.ZERO, TimeDiffUtils.betweenMillis(1_000L, 1_000L))
    }

    @Test
    fun betweenInstantsCalculatesDuration() {
        assertEquals(
            Duration.ofSeconds(5L),
            TimeDiffUtils.betweenInstants(
                Instant.parse("2024-01-02T03:04:05Z"),
                Instant.parse("2024-01-02T03:04:10Z"),
            ),
        )
    }

    @Test
    fun elapsedSinceUsesInjectedClock() {
        val now = Instant.parse("2024-01-02T03:04:10Z")
        val clock = Clock.fixed(now, ZoneOffset.UTC)

        assertEquals(Duration.ofSeconds(5L), TimeDiffUtils.elapsedSinceInstant(now.minusSeconds(5L), clock))
        assertEquals(Duration.ofSeconds(5L), TimeDiffUtils.elapsedSinceMillis(now.minusSeconds(5L).toEpochMilli(), clock))
    }
}
