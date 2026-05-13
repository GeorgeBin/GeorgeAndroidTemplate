package com.georgebindragon.android.base.log

data class LogEntry(
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwableText: String? = null,
    val timestampMillis: Long = System.currentTimeMillis(),
    val threadName: String = Thread.currentThread().name,
)
