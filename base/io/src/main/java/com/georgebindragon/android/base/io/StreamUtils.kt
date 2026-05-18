package com.georgebindragon.android.base.io

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.io.FileAlreadyExistsException

object StreamUtils {
    const val DEFAULT_BUFFER_SIZE = 8 * 1024

    fun copy(
        input: InputStream,
        output: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
    ): Long {
        require(bufferSize > 0) { "bufferSize must be greater than 0." }

        var totalBytes = 0L
        val buffer = ByteArray(bufferSize)
        while (true) {
            val read = input.read(buffer)
            if (read == -1) break
            output.write(buffer, 0, read)
            totalBytes += read
        }
        return totalBytes
    }

    fun saveToFile(
        input: InputStream,
        target: File,
        overwrite: Boolean = true,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
    ): File {
        if (!overwrite && target.exists()) {
            throw FileAlreadyExistsException(target)
        }
        FileUtils.ensureParentDirectory(target)
        target.outputStream().use { output ->
            copy(input, output, bufferSize)
        }
        return target
    }
}
