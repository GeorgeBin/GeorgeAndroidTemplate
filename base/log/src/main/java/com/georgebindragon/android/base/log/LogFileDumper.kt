package com.georgebindragon.android.base.log

import java.io.File

object LogFileDumper {
    fun append(
        file: File,
        text: String,
        writerFactory: LogFileWriterFactory = StreamLogFileWriterFactory,
    ): Result<File> = runCatching {
        file.parentFile?.mkdirs()
        writerFactory.create(file).use { writer ->
            writer.append(text.toByteArray(Charsets.UTF_8))
            writer.flush()
        }
        file
    }

    fun append(
        directory: File,
        fileName: String,
        text: String,
        writerFactory: LogFileWriterFactory = StreamLogFileWriterFactory,
    ): Result<File> = append(
        file = File(directory, fileName),
        text = text,
        writerFactory = writerFactory,
    )
}
