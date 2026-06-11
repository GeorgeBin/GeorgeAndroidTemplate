package com.georgebindragon.android.core.time

import java.time.ZoneId
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultAppTimeRepositoryTest {
    @Test
    fun currentStateMapsProviderTimeToDateAndClockFields() = runTest {
        val provider = FakeTimeProvider(
            systemTimeMillis = 1_735_691_445_000L,
            elapsedRealtimeMillis = 12_345L,
            zoneId = ZoneId.of("UTC"),
        )
        val repository = DefaultAppTimeRepository(
            timeProvider = provider,
            scope = backgroundScope,
        )

        val state = repository.currentState()

        assertEquals(1_735_691_445_000L, state.systemTimeMillis)
        assertEquals(12_345L, state.elapsedRealtimeMillis)
        assertEquals(ZoneId.of("UTC"), state.zoneId)
        assertEquals(2025, state.date.year)
        assertEquals(1, state.date.monthValue)
        assertEquals(1, state.date.dayOfMonth)
        assertEquals(0, state.hour)
        assertEquals(30, state.minute)
        assertEquals(45, state.second)
    }

    @Test
    fun systemCalibrationResetsCalibratedOffset() = runTest {
        val calibratedProvider = CalibratedTimeProvider(
            systemTimeProvider = FakeTimeProvider(
                systemTimeMillis = 1_000L,
                elapsedRealtimeMillis = 10L,
                zoneId = ZoneId.of("UTC"),
            ),
            initialOffsetMillis = 500L,
        )
        val repository = DefaultAppTimeRepository(
            timeProvider = calibratedProvider,
            scope = backgroundScope,
        )

        val result = repository.calibrate(TimeCalibrationStrategy.System)

        assertTrue(result.calibrated)
        assertEquals(1_000L, repository.currentState().systemTimeMillis)
    }

    @Test
    fun unsupportedCalibrationStrategyReturnsNotImplementedResult() = runTest {
        val repository = DefaultAppTimeRepository(
            timeProvider = FakeTimeProvider(
                systemTimeMillis = 1_000L,
                elapsedRealtimeMillis = 10L,
                zoneId = ZoneId.of("UTC"),
            ),
            scope = backgroundScope,
        )

        val result = repository.calibrate(TimeCalibrationStrategy.Ntp)

        assertFalse(result.calibrated)
        assertEquals(TimeCalibrationStrategy.Ntp, result.strategy)
    }
}

private class FakeTimeProvider(
    private val systemTimeMillis: Long,
    private val elapsedRealtimeMillis: Long,
    private val zoneId: ZoneId,
) : SystemTimeProvider {
    override fun currentTimeMillis(): Long = systemTimeMillis

    override fun elapsedRealtimeMillis(): Long = elapsedRealtimeMillis

    override fun zoneId(): ZoneId = zoneId
}
