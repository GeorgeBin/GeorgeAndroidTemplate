package com.georgebindragon.android.base.log

import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant

interface LogFormatter {
    fun format(entry: LogEntry): String
}

class PlainTextLogFormatter : LogFormatter {
    override fun format(entry: LogEntry): String = buildString {
        append(Instant.ofEpochMilli(entry.timestampMillis))
        append(' ')
        append(entry.level.symbol)
        append('/')
        append(entry.tag)
        append(" [")
        append(entry.threadName)
        append("]: ")
        append(entry.message)
        if (entry.throwableText != null) {
            append('\n')
            append(entry.throwableText)
        }
        append('\n')
    }
}

internal fun Throwable?.stackTraceTextOrNull(): String? {
    if (this == null) return null
    val writer = StringWriter()
    printStackTrace(PrintWriter(writer))
    return writer.toString()
}
