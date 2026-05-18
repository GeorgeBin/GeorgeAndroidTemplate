package com.georgebindragon.android.core.network.traffic

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.math.exp
import kotlin.math.ln

class BandwidthEstimatorTest {
    @Test
    fun `first valid measurement becomes current bandwidth`() {
        val estimator = BandwidthEstimator()

        val bandwidth = estimator.record(
            rxBytesDiff = 2_000L,
            txBytesDiff = 1_000L,
            timeDiffMillis = 1_000L,
        )

        assertEquals(16.0, bandwidth?.downloadKbps ?: -1.0, 0.0001)
        assertEquals(0.0, bandwidth?.uploadKbps ?: -1.0, 0.0001)
    }

    @Test
    fun `subsequent valid measurements are smoothed`() {
        val estimator = BandwidthEstimator(decay = 0.2f)
        estimator.record(
            rxBytesDiff = 2_000L,
            txBytesDiff = 2_000L,
            timeDiffMillis = 1_000L,
        )

        val bandwidth = estimator.record(
            rxBytesDiff = 8_000L,
            txBytesDiff = 8_000L,
            timeDiffMillis = 1_000L,
        )

        val expected = exp(0.8 * ln(16.0) + 0.2 * ln(64.0))
        assertEquals(expected, bandwidth?.downloadKbps ?: -1.0, 0.0001)
        assertEquals(expected, bandwidth?.uploadKbps ?: -1.0, 0.0001)
    }

    @Test
    fun `low measurements are ignored`() {
        val estimator = BandwidthEstimator()

        val bandwidth = estimator.record(
            rxBytesDiff = 100L,
            txBytesDiff = 100L,
            timeDiffMillis = 1_000L,
        )

        assertNull(bandwidth)
    }

    @Test
    fun `invalid time and negative byte diffs are ignored`() {
        val estimator = BandwidthEstimator()

        assertNull(
            estimator.record(
                rxBytesDiff = -1L,
                txBytesDiff = 1_000L,
                timeDiffMillis = 0L,
            ),
        )
    }

    @Test
    fun `reset clears previous averages`() {
        val estimator = BandwidthEstimator()
        estimator.record(
            rxBytesDiff = 2_000L,
            txBytesDiff = 2_000L,
            timeDiffMillis = 1_000L,
        )

        estimator.reset()

        val bandwidth = estimator.record(
            rxBytesDiff = 8_000L,
            txBytesDiff = 8_000L,
            timeDiffMillis = 1_000L,
        )
        assertEquals(64.0, bandwidth?.downloadKbps ?: -1.0, 0.0001)
        assertEquals(64.0, bandwidth?.uploadKbps ?: -1.0, 0.0001)
    }
}
