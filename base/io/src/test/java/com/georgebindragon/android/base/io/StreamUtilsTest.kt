package com.georgebindragon.android.base.io

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.io.FileAlreadyExistsException

class StreamUtilsTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun copyInputStreamToOutputStreamReturnsByteCount() {
        val input = ByteArrayInputStream("hello".toByteArray())
        val output = ByteArrayOutputStream()

        val bytes = StreamUtils.copy(input, output, bufferSize = 2)

        assertEquals(5L, bytes)
        assertArrayEquals("hello".toByteArray(), output.toByteArray())
    }

    @Test
    fun saveToFileCreatesParentDirectoryAndWritesContent() {
        val input = ByteArrayInputStream("hello".toByteArray())
        val target = File(temporaryFolder.root, "nested/file.txt")

        val result = StreamUtils.saveToFile(input, target)

        assertEquals(target, result)
        assertEquals("hello", target.readText())
    }

    @Test
    fun saveToFileOverwritesByDefault() {
        val target = temporaryFolder.newFile("file.txt")
        target.writeText("old")

        StreamUtils.saveToFile(ByteArrayInputStream("new".toByteArray()), target)

        assertEquals("new", target.readText())
    }

    @Test(expected = FileAlreadyExistsException::class)
    fun saveToFileCanRejectExistingTarget() {
        val target = temporaryFolder.newFile("file.txt")

        StreamUtils.saveToFile(ByteArrayInputStream("new".toByteArray()), target, overwrite = false)
    }
}
