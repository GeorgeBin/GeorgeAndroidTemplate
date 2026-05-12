package com.georgebindragon.android.base.log

import android.content.Context
import android.util.Log
import com.georgebindragon.android.base.data.FileStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant

data class LogEntry(
    val level: String,
    val tag: String,
    val message: String,
    val timestamp: String = Instant.now().toString(),
)

object TemplateLogger {
    private val entriesFlow = MutableStateFlow<List<LogEntry>>(emptyList())
    private var fileStore: FileStore? = null
    private const val maxEntries = 300

    val entries: StateFlow<List<LogEntry>> = entriesFlow.asStateFlow()

    fun initialize(context: Context) {
        fileStore = FileStore(context.applicationContext)
        i("TemplateLogger", "logger initialized")
    }

    fun d(tag: String, message: String) = write("D", tag, message)
    fun i(tag: String, message: String) = write("I", tag, message)
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        write("E", tag, buildString {
            append(message)
            if (throwable != null) append(" | ").append(throwable.message)
        })
    }

    fun export(): String {
        val content = entriesFlow.value.joinToString(separator = "\n") {
            "${it.timestamp} ${it.level}/${it.tag}: ${it.message}"
        }
        fileStore?.writeText("logs/template.log", content)
        return content
    }

    private fun write(level: String, tag: String, message: String) {
        when (level) {
            "D" -> Log.d(tag, message)
            "I" -> Log.i(tag, message)
            "E" -> Log.e(tag, message)
        }
        val newEntry = LogEntry(level = level, tag = tag, message = message)
        entriesFlow.value = (listOf(newEntry) + entriesFlow.value).take(maxEntries)
    }
}

object TemplateLogFacade {
    @JvmStatic
    fun info(tag: String, message: String) = TemplateLogger.i(tag, message)
}
