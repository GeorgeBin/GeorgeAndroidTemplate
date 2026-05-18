package com.georgebindragon.android.base.security.hex

import org.junit.Assert.assertEquals
import org.junit.Test

class HexUtilsTest {
    @Test
    fun lowercaseFormatsBytesAsLowercaseHex() {
        val bytes = byteArrayOf(0x00, 0x0f, 0x10, 0xff.toByte())

        assertEquals("000f10ff", HexUtils.lowercase(bytes))
    }

    @Test
    fun lowercaseFormatsEmptyBytes() {
        assertEquals("", HexUtils.lowercase(ByteArray(0)))
    }
}
