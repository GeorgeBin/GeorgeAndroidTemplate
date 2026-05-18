package com.georgebindragon.android.base.crash

import com.georgebindragon.android.base.log.GLog
import com.georgebindragon.android.base.log.LogEntry
import com.georgebindragon.android.base.log.Logger
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class CrashReporterTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private var originalHandler: Thread.UncaughtExceptionHandler? = null

    @Before
    fun setUp() {
        originalHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(null)
        CrashReporter.resetForTest()
    }

    @After
    fun tearDown() {
        Thread.setDefaultUncaughtExceptionHandler(originalHandler)
        CrashReporter.resetForTest()
        GLog.reset()
    }

    @Test
    fun initializeInstallsUncaughtExceptionHandler() {
        CrashReporter.initialize(
            config = CrashConfig(directory = temporaryFolder.newFolder("crash")),
        )

        assertNotNull(Thread.getDefaultUncaughtExceptionHandler())
    }

    @Test
    fun uncaughtExceptionWritesCrashFileAndInvokesCallback() {
        val crashDirectory = temporaryFolder.newFolder("crash")
        val callbackResults = mutableListOf<CrashLogResult>()
        val logger = RecordingLogger()
        GLog.install(logger)
        CrashReporter.initialize(
            config = CrashConfig(
                directory = crashDirectory,
                fileNamePolicy = FixedCrashFileNamePolicy("crash.log"),
            ),
            onCrashLogSaved = { callbackResults += it },
        )

        val throwable = IllegalStateException("boom")
        Thread.getDefaultUncaughtExceptionHandler()
            ?.uncaughtException(Thread.currentThread(), throwable)

        val crashFile = File(crashDirectory, "crash.log")
        assertTrue(crashFile.exists())
        assertTrue(crashFile.readText().contains("IllegalStateException: boom"))
        assertEquals(1, callbackResults.size)
        assertEquals(crashFile, callbackResults.single().file)
        assertTrue(callbackResults.single().saved)
        assertEquals(Thread.currentThread().name, callbackResults.single().threadName)
        assertTrue(logger.entries.any { it.tag == "CrashReporter" && it.message.contains("Uncaught exception") })
        assertTrue(logger.flushed)
    }

    @Test
    fun uncaughtExceptionDelegatesToPreviousHandler() {
        val throwable = IllegalArgumentException("boom")
        val delegated = mutableListOf<Throwable>()
        Thread.setDefaultUncaughtExceptionHandler { _, error -> delegated += error }
        CrashReporter.initialize(
            config = CrashConfig(
                directory = temporaryFolder.newFolder("crash"),
                fileNamePolicy = FixedCrashFileNamePolicy("crash.log"),
            ),
        )

        Thread.getDefaultUncaughtExceptionHandler()
            ?.uncaughtException(Thread.currentThread(), throwable)

        assertEquals(listOf(throwable), delegated)
    }

    @Test
    fun initializeMoreThanOnceDoesNotWrapCurrentHandlerAgain() {
        CrashReporter.initialize(
            config = CrashConfig(directory = temporaryFolder.newFolder("crash-1")),
        )
        val firstHandler = Thread.getDefaultUncaughtExceptionHandler()

        CrashReporter.initialize(
            config = CrashConfig(directory = temporaryFolder.newFolder("crash-2")),
        )

        assertSame(firstHandler, Thread.getDefaultUncaughtExceptionHandler())
    }

    @Test
    fun failedSaveStillInvokesCallbackAndDelegates() {
        val fileInsteadOfDirectory = temporaryFolder.newFile("crash")
        val callbackResults = mutableListOf<CrashLogResult>()
        val delegated = mutableListOf<Throwable>()
        Thread.setDefaultUncaughtExceptionHandler { _, error -> delegated += error }
        CrashReporter.initialize(
            config = CrashConfig(
                directory = fileInsteadOfDirectory,
                fileNamePolicy = FixedCrashFileNamePolicy("crash.log"),
            ),
            onCrashLogSaved = { callbackResults += it },
        )

        val throwable = IllegalStateException("boom")
        Thread.getDefaultUncaughtExceptionHandler()
            ?.uncaughtException(Thread.currentThread(), throwable)

        assertEquals(1, callbackResults.size)
        assertFalse(callbackResults.single().saved)
        assertNotNull(callbackResults.single().writeError)
        assertEquals(listOf(throwable), delegated)
    }

    private class FixedCrashFileNamePolicy(
        private val fileName: String,
    ) : CrashFileNamePolicy {
        override fun nextFileName(timestampMillis: Long, index: Int): String = fileName
    }

    private class RecordingLogger : Logger {
        val entries = mutableListOf<LogEntry>()
        var flushed = false

        override fun log(entry: LogEntry) {
            entries += entry
        }

        override fun flush() {
            flushed = true
        }

        override fun close() = Unit
    }
}
