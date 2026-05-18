package com.georgebindragon.android.core.network.traffic

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TrafficReadingTest {
    @Test
    fun `non-negative rx and tx are supported`() {
        assertTrue(TrafficReading(rxBytes = 0L, txBytes = 0L, timestampMillis = 0L).isSupported)
    }

    @Test
    fun `negative rx or tx is unsupported`() {
        assertFalse(TrafficReading(rxBytes = -1L, txBytes = 0L, timestampMillis = 0L).isSupported)
        assertFalse(TrafficReading(rxBytes = 0L, txBytes = -1L, timestampMillis = 0L).isSupported)
    }
}
