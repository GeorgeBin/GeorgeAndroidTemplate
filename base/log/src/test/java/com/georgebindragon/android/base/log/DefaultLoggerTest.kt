package com.georgebindragon.android.base.log

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class DefaultLoggerTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun globalFacadeForwardsToInstalledLogger() {
        val logger = RecordingLogger()
        GLog.install(logger)

        GLog.i("Test", "message")

        assertEquals(1, logger.entries.size)
        assertEquals(LogLevel.Info, logger.entries.single().level)
        assertEquals("Test", logger.entries.single().tag)
        assertEquals("message", logger.entries.single().message)
    }

    @Test
    fun fileSinkWritesFormattedEntriesAfterFlush() {
        val directory = temporaryFolder.newFolder("logs")
        val logger = DefaultLogger(
            LogConfig(
                sinks = listOf(
                    FileLogSink(
                        FileLogConfig(
                            directory = directory,
                            fileNamePolicy = FixedLogFileNamePolicy("app.log"),
                        ),
                    ),
                ),
            ),
        )

        logger.log(LogEntry(LogLevel.Info, "Test", "hello", timestampMillis = 0L, threadName = "main"))
        logger.flush()
        logger.close()

        val content = File(directory, "app.log").readText()
        assertTrue(content.contains("I/Test [main]: hello"))
    }

    @Test
    fun fileSinkRotatesWhenCurrentFileExceedsLimit() {
        val directory = temporaryFolder.newFolder("logs")
        val logger = DefaultLogger(
            LogConfig(
                sinks = listOf(
                    FileLogSink(
                        FileLogConfig(
                            directory = directory,
                            fileNamePolicy = IndexedLogFileNamePolicy,
                            maxFileBytes = 80L,
                            maxTotalBytes = 1_000L,
                        ),
                    ),
                ),
            ),
        )

        repeat(8) { index ->
            logger.log(
                LogEntry(
                    level = LogLevel.Info,
                    tag = "Test",
                    message = "message-$index-abcdefghijklmnopqrstuvwxyz",
                    timestampMillis = 0L,
                    threadName = "main",
                ),
            )
        }
        logger.flush()
        logger.close()

        assertTrue(directory.listFiles().orEmpty().size > 1)
    }

    @Test
    fun fileSinkTrimsOldFilesWhenDirectoryExceedsLimit() {
        val directory = temporaryFolder.newFolder("logs")
        File(directory, "old-1.log").writeText("x".repeat(100))
        File(directory, "old-2.log").writeText("x".repeat(100))
        File(directory, "old-1.log").setLastModified(1L)
        File(directory, "old-2.log").setLastModified(2L)

        val logger = DefaultLogger(
            LogConfig(
                sinks = listOf(
                    FileLogSink(
                        FileLogConfig(
                            directory = directory,
                            fileNamePolicy = IndexedLogFileNamePolicy,
                            maxFileBytes = 120L,
                            maxTotalBytes = 150L,
                        ),
                    ),
                ),
            ),
        )

        logger.log(LogEntry(LogLevel.Info, "Test", "new", timestampMillis = 0L, threadName = "main"))
        logger.flush()
        logger.close()

        val totalBytes = directory.listFiles().orEmpty().sumOf { it.length() }
        assertTrue(totalBytes <= 150L)
        assertTrue(File(directory, "old-1.log").exists().not())
    }

    @Test
    fun fileSinkUsesConfiguredWriterFactory() {
        val directory = temporaryFolder.newFolder("logs")
        val createdFiles = mutableListOf<File>()
        val logger = DefaultLogger(
            LogConfig(
                sinks = listOf(
                    FileLogSink(
                        FileLogConfig(
                            directory = directory,
                            fileNamePolicy = FixedLogFileNamePolicy("custom.log"),
                            writerFactory = LogFileWriterFactory { file ->
                                createdFiles += file
                                StreamLogFileWriter(file)
                            },
                        ),
                    ),
                ),
            ),
        )

        logger.log(LogEntry(LogLevel.Warn, "Test", "custom writer"))
        logger.flush()
        logger.close()

        assertEquals(listOf(File(directory, "custom.log")), createdFiles)
        assertTrue(File(directory, "custom.log").readText().contains("custom writer"))
    }

    @Test
    fun closeCanBeCalledMoreThanOnce() {
        val logger = DefaultLogger(LogConfig(sinks = listOf(RecordingSink())))

        logger.close()
        logger.close()

        logger.log(LogEntry(LogLevel.Info, "Test", "ignored"))
        logger.flush()
    }

    private class RecordingLogger : Logger {
        val entries = mutableListOf<LogEntry>()

        override fun log(entry: LogEntry) {
            entries += entry
        }

        override fun flush() = Unit

        override fun close() = Unit
    }

    private class FixedLogFileNamePolicy(
        private val fileName: String,
    ) : LogFileNamePolicy {
        override fun nextFileName(timestampMillis: Long, index: Int): String = fileName
    }

    private object IndexedLogFileNamePolicy : LogFileNamePolicy {
        override fun nextFileName(timestampMillis: Long, index: Int): String = "log-$index.log"
    }

    private class RecordingSink : LogSink {
        override fun write(entry: LogEntry) = Unit
    }
}
