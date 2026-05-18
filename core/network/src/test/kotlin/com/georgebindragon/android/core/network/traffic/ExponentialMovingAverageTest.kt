package com.georgebindragon.android.core.network.traffic

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.exp
import kotlin.math.ln

class ExponentialMovingAverageTest {
    @Test
    fun `first measurement becomes average`() {
        val average = ExponentialMovingAverage(decay = 0.2f)

        average.add(16.0)

        assertEquals(16.0, average.average(), 0.0001)
    }

    @Test
    fun `following measurement uses exponential moving average`() {
        val average = ExponentialMovingAverage(decay = 0.2f)
        average.add(16.0)

        average.add(64.0)

        assertEquals(exp(0.8 * ln(16.0) + 0.2 * ln(64.0)), average.average(), 0.0001)
    }

    @Test
    fun `reset marks average undefined`() {
        val average = ExponentialMovingAverage(decay = 0.2f)
        average.add(16.0)

        average.reset()

        assertEquals(-1.0, average.average(), 0.0001)
    }
}
