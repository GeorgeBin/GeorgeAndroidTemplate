package com.georgebindragon.android.base.log

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class LogFileDumperTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun appendCreatesParentDirectoryAndFile() {
        val file = File(temporaryFolder.root, "logs/app.log")

        val result = LogFileDumper.append(file, "first\n")

        assertTrue(result.isSuccess)
        assertEquals(file, result.getOrThrow())
        assertEquals("first\n", file.readText())
    }

    @Test
    fun appendDoesNotOverwriteExistingContent() {
        val file = temporaryFolder.newFile("app.log")
        file.writeText("first\n")

        LogFileDumper.append(file, "second\n").getOrThrow()

        assertEquals("first\nsecond\n", file.readText())
    }

    @Test
    fun appendReturnsFailureWhenWriterFails() {
        val file = File(temporaryFolder.root, "app.log")
        val failure = IllegalStateException("writer failed")

        val result = LogFileDumper.append(
            file = file,
            text = "ignored",
            writerFactory = LogFileWriterFactory { throw failure },
        )

        assertTrue(result.isFailure)
        assertEquals(failure, result.exceptionOrNull())
    }

    @Test
    fun appendCanTargetDirectoryAndFileName() {
        val directory = temporaryFolder.newFolder("logs")

        val result = LogFileDumper.append(directory, "app.log", "message\n")

        assertEquals(File(directory, "app.log"), result.getOrThrow())
        assertEquals("message\n", File(directory, "app.log").readText())
    }
}
