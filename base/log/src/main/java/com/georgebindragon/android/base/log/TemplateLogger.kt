package com.georgebindragon.android.base.log

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

object TemplateLogger {
    private val memorySink = InMemoryLogSink()

    val entries: StateFlow<List<LogEntry>> = memorySink.entries

    fun initialize(context: Context) {
        GLog.install(
            DefaultLogger(
                LogConfig(
                    sinks = listOf(
                        LogcatLogSink,
                        FileLogSink(FileLogConfig.default(context)),
                        memorySink,
                    ),
                ),
            ),
        )
        i("TemplateLogger", "logger initialized")
    }

    fun d(tag: String, message: String) = GLog.d(tag, message)

    fun i(tag: String, message: String) = GLog.i(tag, message)

    fun e(tag: String, message: String, throwable: Throwable? = null) =
        GLog.e(tag, message, throwable)

    fun export(): String {
        GLog.flush()
        return entries.value.reversed().joinToString(separator = "\n") {
            PlainTextLogFormatter().format(it).trimEnd()
        }
    }
}

object TemplateLogFacade {
    @JvmStatic
    fun info(tag: String, message: String) = GLog.i(tag, message)
}
