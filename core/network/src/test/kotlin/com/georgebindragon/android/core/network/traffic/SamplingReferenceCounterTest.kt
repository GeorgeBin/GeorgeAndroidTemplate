package com.georgebindragon.android.core.network.traffic

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SamplingReferenceCounterTest {
    @Test
    fun `first increment starts sampling and later increments share task`() {
        val counter = SamplingReferenceCounter()

        assertTrue(counter.increment())
        assertFalse(counter.increment())
        assertFalse(counter.increment())
    }

    @Test
    fun `last decrement stops sampling`() {
        val counter = SamplingReferenceCounter()
        counter.increment()
        counter.increment()

        assertFalse(counter.decrement())
        assertTrue(counter.decrement())
        assertFalse(counter.decrement())
    }

    @Test
    fun `reset clears active references`() {
        val counter = SamplingReferenceCounter()
        counter.increment()
        counter.increment()

        counter.reset()

        assertTrue(counter.increment())
    }
}
