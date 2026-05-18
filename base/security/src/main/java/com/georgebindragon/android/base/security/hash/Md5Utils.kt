package com.georgebindragon.android.base.security.hash

import com.georgebindragon.android.base.security.hex.HexUtils
import java.io.File
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * MD5 is provided only for compatibility with legacy protocols and checksums.
 * Do not use it as a recommended secure encryption, password, or signature primitive.
 */
object Md5Utils {
    fun digest(bytes: ByteArray): ByteArray = digestBytes("MD5", bytes)

    fun digest(
        text: String,
        charset: Charset = Charsets.UTF_8,
    ): ByteArray = digest(text.toByteArray(charset))

    fun digest(file: File): ByteArray = digestFile("MD5", file)

    fun lowercaseHex(bytes: ByteArray): String = HexUtils.lowercase(digest(bytes))

    fun lowercaseHex(
        text: String,
        charset: Charset = Charsets.UTF_8,
    ): String = HexUtils.lowercase(digest(text, charset))

    fun lowercaseHex(file: File): String = HexUtils.lowercase(digest(file))
}

internal fun digestBytes(
    algorithm: String,
    bytes: ByteArray,
): ByteArray = MessageDigest.getInstance(algorithm).digest(bytes)

internal fun digestFile(
    algorithm: String,
    file: File,
): ByteArray {
    val digest = MessageDigest.getInstance(algorithm)
    file.inputStream().use { input ->
        val buffer = ByteArray(8 * 1024)
        while (true) {
            val read = input.read(buffer)
            if (read == -1) break
            digest.update(buffer, 0, read)
        }
    }
    return digest.digest()
}
