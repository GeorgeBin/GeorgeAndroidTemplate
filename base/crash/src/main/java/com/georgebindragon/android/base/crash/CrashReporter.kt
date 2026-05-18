package com.georgebindragon.android.base.crash

import android.content.Context
import com.georgebindragon.android.base.log.GLog
import com.georgebindragon.android.base.log.LogFileDumper
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import java.util.concurrent.atomic.AtomicBoolean

object CrashReporter {
    private const val TAG = "CrashReporter"

    private val initialized = AtomicBoolean(false)

    fun initialize(
        context: Context,
        config: CrashConfig = CrashConfig.default(context),
        onCrashLogSaved: (CrashLogResult) -> Unit = {},
    ) {
        initialize(config, onCrashLogSaved)
    }

    internal fun initialize(
        config: CrashConfig,
        onCrashLogSaved: (CrashLogResult) -> Unit = {},
    ) {
        if (!initialized.compareAndSet(false, true)) return

        val previousHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            Handler(
                config = config,
                previousHandler = previousHandler,
                onCrashLogSaved = onCrashLogSaved,
            ),
        )
    }

    internal fun resetForTest() {
        initialized.set(false)
    }

    private class Handler(
        private val config: CrashConfig,
        private val previousHandler: Thread.UncaughtExceptionHandler?,
        private val onCrashLogSaved: (CrashLogResult) -> Unit,
    ) : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(thread: Thread, throwable: Throwable) {
            val crashText = formatCrash(thread, throwable)
            GLog.e(TAG, "Uncaught exception on thread ${thread.name}", throwable)
            GLog.flush()

            val saveResult = saveCrashLog(crashText)
            val result = CrashLogResult(
                file = saveResult.getOrNull(),
                throwable = throwable,
                threadName = thread.name,
                writeError = saveResult.exceptionOrNull(),
            )

            runCatching { onCrashLogSaved(result) }
                .onFailure { GLog.e(TAG, "Crash log saved callback failed", it) }

            previousHandler?.uncaughtException(thread, throwable)
        }

        private fun saveCrashLog(text: String): Result<File> {
            val timestampMillis = System.currentTimeMillis()
            repeat(1_000) { index ->
                val fileName = config.fileNamePolicy.nextFileName(timestampMillis, index)
                val file = File(config.directory, fileName)
                if (!file.exists()) {
                    return LogFileDumper.append(file, text)
                }
            }
            return LogFileDumper.append(
                config.directory,
                "crash-$timestampMillis.log",
                text,
            )
        }

        private fun formatCrash(thread: Thread, throwable: Throwable): String = buildString {
            append("timestamp: ")
            append(Instant.ofEpochMilli(System.currentTimeMillis()))
            append('\n')
            append("thread: ")
            append(thread.name)
            append('\n')
            append("exception: ")
            append(throwable::class.java.name)
            append('\n')
            append("message: ")
            append(throwable.message.orEmpty())
            append('\n')
            append('\n')
            append(throwable.stackTraceText())
            if (!endsWith('\n')) append('\n')
        }

        private fun Throwable.stackTraceText(): String {
            val writer = StringWriter()
            printStackTrace(PrintWriter(writer))
            return writer.toString()
        }
    }
}
