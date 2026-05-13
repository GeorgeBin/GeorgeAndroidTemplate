package com.georgebindragon.android.base.log

interface LogSink {
    fun write(entry: LogEntry)
    fun flush() = Unit
}
