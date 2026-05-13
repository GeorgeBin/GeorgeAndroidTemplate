package com.georgebindragon.android.base.log

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

interface LogFileNamePolicy {
    fun nextFileName(timestampMillis: Long, index: Int): String
}

class TimestampLogFileNamePolicy(
    private val zoneId: ZoneId = ZoneId.systemDefault(),
) : LogFileNamePolicy {
    private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")

    override fun nextFileName(timestampMillis: Long, index: Int): String {
        val timestamp = Instant.ofEpochMilli(timestampMillis).atZone(zoneId).format(formatter)
        return "$timestamp-$index.log"
    }
}
