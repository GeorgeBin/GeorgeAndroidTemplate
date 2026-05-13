package com.georgebindragon.android.base.log

interface Logger {
    fun log(entry: LogEntry)
    fun flush()
    fun close()
}
