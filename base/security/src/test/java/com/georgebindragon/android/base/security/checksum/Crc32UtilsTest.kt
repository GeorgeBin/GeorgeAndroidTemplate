package com.georgebindragon.android.base.security.checksum

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class Crc32UtilsTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun checksumSupportsByteArrayStringAndFile() {
        val file = temporaryFolder.newFile("abc.txt")
        file.writeText("abc")

        assertEquals(891568578L, Crc32Utils.checksum("abc"))
        assertEquals(Crc32Utils.checksum("abc"), Crc32Utils.checksum("abc".toByteArray()))
        assertEquals(Crc32Utils.checksum("abc"), Crc32Utils.checksum(file))
    }

    @Test
    fun lowercaseHexReturnsEightCharacterLowercaseHex() {
        val file = temporaryFolder.newFile("abc.txt")
        file.writeText("abc")

        assertEquals("352441c2", Crc32Utils.lowercaseHex("abc"))
        assertEquals(Crc32Utils.lowercaseHex("abc"), Crc32Utils.lowercaseHex("abc".toByteArray()))
        assertEquals(Crc32Utils.lowercaseHex("abc"), Crc32Utils.lowercaseHex(file))
    }

    @Test
    fun emptyContentHasZeroChecksum() {
        assertEquals(0L, Crc32Utils.checksum(ByteArray(0)))
        assertEquals("00000000", Crc32Utils.lowercaseHex(ByteArray(0)))
    }
}
