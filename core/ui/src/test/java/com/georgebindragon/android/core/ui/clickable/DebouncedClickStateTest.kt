package com.georgebindragon.android.core.ui.clickable

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DebouncedClickStateTest {
    @Test
    fun firstClickIsAllowed() {
        var now = 0L
        var clickCount = 0
        val state = DebouncedClickState(
            intervalMillis = 500L,
            nowMillis = { now },
        )

        val clicked = state.tryClick { clickCount++ }

        assertTrue(clicked)
        assertEquals(1, clickCount)
    }

    @Test
    fun clickInsideIntervalIsIgnored() {
        var now = 0L
        var clickCount = 0
        val state = DebouncedClickState(
            intervalMillis = 500L,
            nowMillis = { now },
        )

        state.tryClick { clickCount++ }
        now = 499L
        val clicked = state.tryClick { clickCount++ }

        assertFalse(clicked)
        assertEquals(1, clickCount)
    }

    @Test
    fun clickAtIntervalBoundaryIsAllowed() {
        var now = 0L
        var clickCount = 0
        val state = DebouncedClickState(
            intervalMillis = 500L,
            nowMillis = { now },
        )

        state.tryClick { clickCount++ }
        now = 500L
        val clicked = state.tryClick { clickCount++ }

        assertTrue(clicked)
        assertEquals(2, clickCount)
    }
}
