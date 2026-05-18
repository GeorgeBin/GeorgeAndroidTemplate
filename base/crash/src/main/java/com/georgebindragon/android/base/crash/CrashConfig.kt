package com.georgebindragon.android.base.crash

import android.content.Context
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class CrashConfig(
    val directory: File,
    val fileNamePolicy: CrashFileNamePolicy = TimestampCrashFileNamePolicy(),
) {
    companion object {
        fun default(context: Context): CrashConfig {
            val appContext = context.applicationContext
            val externalCrashDirectory = appContext.getExternalFilesDir(null)?.resolve("crash")
            return CrashConfig(
                directory = externalCrashDirectory ?: File(appContext.filesDir, "crash"),
            )
        }
    }
}

interface CrashFileNamePolicy {
    fun nextFileName(timestampMillis: Long, index: Int): String
}

class TimestampCrashFileNamePolicy(
    private val zoneId: ZoneId = ZoneId.systemDefault(),
) : CrashFileNamePolicy {
    private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS")

    override fun nextFileName(timestampMillis: Long, index: Int): String {
        val timestamp = Instant.ofEpochMilli(timestampMillis).atZone(zoneId).format(formatter)
        val suffix = if (index == 0) "" else "-$index"
        return "crash-$timestamp$suffix.log"
    }
}
