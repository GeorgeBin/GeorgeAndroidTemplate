package com.georgebindragon.android.base.log

import java.io.Closeable
import java.io.File

class FileLogSink(
    private val config: FileLogConfig,
) : LogSink, Closeable {
    private var currentFile: File? = null
    private var currentWriter: LogFileWriter? = null
    private var fileIndex = 0
    private var disabled = false

    override fun write(entry: LogEntry) {
        if (disabled) return

        runCatching {
            config.directory.mkdirs()
            val bytes = config.formatter.format(entry).toByteArray(Charsets.UTF_8)
            ensureWriter(bytes.size.toLong())
            currentWriter?.append(bytes)
        }.onFailure { throwable ->
            disabled = true
            closeCurrentWriter()
            LogcatLogSink.writeFallback("FileLogSink", throwable)
        }
    }

    override fun flush() {
        if (disabled) return
        runCatching { currentWriter?.flush() }
            .onFailure { throwable ->
                disabled = true
                LogcatLogSink.writeFallback("FileLogSink", throwable)
            }
    }

    override fun close() {
        closeCurrentWriter()
    }

    private fun ensureWriter(nextBytes: Long) {
        val file = currentFile
        val shouldRotate = file == null || file.length() + nextBytes > config.maxFileBytes
        if (shouldRotate) rotate()
    }

    private fun rotate() {
        closeCurrentWriter()
        fileIndex += 1
        currentFile = createNextFile()
        currentWriter = config.writerFactory.create(currentFile!!)
        trimDirectory()
    }

    private fun createNextFile(): File {
        val previousFile = currentFile
        repeat(1_000) {
            val candidate = File(
                config.directory,
                config.fileNamePolicy.nextFileName(
                    timestampMillis = System.currentTimeMillis(),
                    index = fileIndex,
                ),
            )
            if (!candidate.exists()) return candidate
            if (candidate != previousFile && candidate.length() < config.maxFileBytes) return candidate
            fileIndex += 1
        }
        return File(config.directory, "log-${System.currentTimeMillis()}-$fileIndex.log")
    }

    private fun trimDirectory() {
        val files = config.directory
            .listFiles { file -> file.isFile }
            ?.sortedWith(compareBy<File> { it.lastModified() }.thenBy { it.name })
            ?: return

        var totalBytes = files.sumOf { it.length() }
        for (file in files) {
            if (totalBytes <= config.maxTotalBytes) break
            if (file == currentFile) continue
            val length = file.length()
            if (file.delete()) totalBytes -= length
        }
    }

    private fun closeCurrentWriter() {
        runCatching { currentWriter?.flush() }
        runCatching { currentWriter?.close() }
        currentWriter = null
    }
}
