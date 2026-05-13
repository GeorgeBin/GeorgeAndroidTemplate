package com.georgebindragon.android.base.log

import android.util.Log

object LogcatLogSink : LogSink {
    override fun write(entry: LogEntry) {
        val message = if (entry.throwableText == null) {
            entry.message
        } else {
            "${entry.message}\n${entry.throwableText}"
        }
        when (entry.level) {
            LogLevel.Verbose -> Log.v(entry.tag, message)
            LogLevel.Debug -> Log.d(entry.tag, message)
            LogLevel.Info -> Log.i(entry.tag, message)
            LogLevel.Warn -> Log.w(entry.tag, message)
            LogLevel.Error -> Log.e(entry.tag, message)
        }
    }

    internal fun writeFallback(tag: String, throwable: Throwable) {
        runCatching {
            Log.e(tag, "Log sink failure", throwable)
        }
    }
}
