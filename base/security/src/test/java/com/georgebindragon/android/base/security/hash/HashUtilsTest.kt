package com.georgebindragon.android.base.security.hash

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class HashUtilsTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun md5SupportsByteArrayStringAndFile() {
        val file = temporaryFolder.newFile("abc.txt")
        file.writeText("abc")
        val expected = "900150983cd24fb0d6963f7d28e17f72"

        assertEquals(expected, Md5Utils.lowercaseHex("abc"))
        assertEquals(expected, Md5Utils.lowercaseHex("abc".toByteArray()))
        assertEquals(expected, Md5Utils.lowercaseHex(file))
    }

    @Test
    fun sha1SupportsByteArrayStringAndFile() {
        val file = temporaryFolder.newFile("abc.txt")
        file.writeText("abc")
        val expected = "a9993e364706816aba3e25717850c26c9cd0d89d"

        assertEquals(expected, Sha1Utils.lowercaseHex("abc"))
        assertEquals(expected, Sha1Utils.lowercaseHex("abc".toByteArray()))
        assertEquals(expected, Sha1Utils.lowercaseHex(file))
    }

    @Test
    fun sha256SupportsByteArrayStringAndFile() {
        val file = temporaryFolder.newFile("abc.txt")
        file.writeText("abc")
        val expected = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"

        assertEquals(expected, Sha256Utils.lowercaseHex("abc"))
        assertEquals(expected, Sha256Utils.lowercaseHex("abc".toByteArray()))
        assertEquals(expected, Sha256Utils.lowercaseHex(file))
    }

    @Test
    fun emptyContentHashesAreStable() {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Md5Utils.lowercaseHex(ByteArray(0)))
        assertEquals("da39a3ee5e6b4b0d3255bfef95601890afd80709", Sha1Utils.lowercaseHex(ByteArray(0)))
        assertEquals(
            "e3b0c44298fc1c149afbf4c8996fb924" +
                "27ae41e4649b934ca495991b7852b855",
            Sha256Utils.lowercaseHex(ByteArray(0)),
        )
    }
}
