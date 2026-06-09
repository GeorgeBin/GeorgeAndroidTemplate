package com.georgebindragon.android.base.common

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SafeCallTest {
    @Test
    fun safeCallReturnsSuccess() = runTest {
        val result = safeCall(errorCode = "ignored") { "ok" }

        assertEquals(AppResult.Success("ok"), result)
    }

    @Test
    fun safeCallWrapsFailure() = runTest {
        val result = safeCall(errorCode = "boom") { error("failed") }

        assertTrue(result is AppResult.Failure)
        assertEquals("boom", (result as AppResult.Failure).error.code)
    }
}
