package com.georgebindragon.android.base.log

import java.io.Closeable
import java.io.File
import java.io.FileOutputStream

interface LogFileWriter : Closeable {
    fun append(bytes: ByteArray)
    fun flush()
}

fun interface LogFileWriterFactory {
    fun create(file: File): LogFileWriter
}

object StreamLogFileWriterFactory : LogFileWriterFactory {
    override fun create(file: File): LogFileWriter = StreamLogFileWriter(file)
}

class StreamLogFileWriter(
    file: File,
) : LogFileWriter {
    private val outputStream = FileOutputStream(file, true)

    override fun append(bytes: ByteArray) {
        outputStream.write(bytes)
    }

    override fun flush() {
        outputStream.flush()
    }

    override fun close() {
        outputStream.close()
    }
}
