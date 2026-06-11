package com.georgebindragon.android.core.timer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Test

class SharedTimerManagerTest {
    @Test
    fun sameIntervalReturnsSharedTicker() {
        val manager = SharedTimerManager(
            timerSource = FakeTimerSource(),
            scope = TestScope(),
        )

        val first = manager.ticks(1_000L)
        val second = manager.ticks(1_000L)

        assertSame(first, second)
    }

    @Test
    fun differentIntervalsReturnDifferentTickers() {
        val manager = SharedTimerManager(
            timerSource = FakeTimerSource(),
            scope = TestScope(),
        )

        val first = manager.ticks(1_000L)
        val second = manager.ticks(2_000L)

        assertNotSame(first, second)
    }
}

private class FakeTimerSource : TimerSource {
    override fun ticks(intervalMillis: Long): Flow<TimerTick> {
        return flowOf(
            TimerTick(
                intervalMillis = intervalMillis,
                tickCount = 1L,
                elapsedRealtimeMillis = 1L,
            ),
        )
    }
}
