package com.georgebindragon.android.base.security.checksum

import java.io.File
import java.nio.charset.Charset
import java.util.zip.CRC32

object Crc32Utils {
    private const val BUFFER_SIZE = 8 * 1024

    fun checksum(bytes: ByteArray): Long {
        val crc32 = CRC32()
        crc32.update(bytes)
        return crc32.value
    }

    fun checksum(
        text: String,
        charset: Charset = Charsets.UTF_8,
    ): Long = checksum(text.toByteArray(charset))

    fun checksum(file: File): Long {
        val crc32 = CRC32()
        file.inputStream().use { input ->
            val buffer = ByteArray(BUFFER_SIZE)
            while (true) {
                val read = input.read(buffer)
                if (read == -1) break
                crc32.update(buffer, 0, read)
            }
        }
        return crc32.value
    }

    fun lowercaseHex(bytes: ByteArray): String = checksum(bytes).toLowercaseHex()

    fun lowercaseHex(
        text: String,
        charset: Charset = Charsets.UTF_8,
    ): String = checksum(text, charset).toLowercaseHex()

    fun lowercaseHex(file: File): String = checksum(file).toLowercaseHex()

    private fun Long.toLowercaseHex(): String = toString(radix = 16).padStart(8, '0')
}
