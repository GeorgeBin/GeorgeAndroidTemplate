package com.georgebindragon.android.base.log

import android.content.Context
import java.io.File

data class FileLogConfig(
    val directory: File,
    val fileNamePolicy: LogFileNamePolicy = TimestampLogFileNamePolicy(),
    val maxFileBytes: Long = 2L * 1024L * 1024L,
    val maxTotalBytes: Long = 20L * 1024L * 1024L,
    val formatter: LogFormatter = PlainTextLogFormatter(),
    val writerFactory: LogFileWriterFactory = StreamLogFileWriterFactory,
) {
    init {
        require(maxFileBytes > 0L) { "maxFileBytes must be greater than 0." }
        require(maxTotalBytes >= maxFileBytes) {
            "maxTotalBytes must be greater than or equal to maxFileBytes."
        }
    }

    companion object {
        fun default(context: Context): FileLogConfig =
            FileLogConfig(directory = File(context.applicationContext.filesDir, "logs"))
    }
}
