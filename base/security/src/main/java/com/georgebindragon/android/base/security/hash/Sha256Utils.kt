package com.georgebindragon.android.base.security.hash

import com.georgebindragon.android.base.security.hex.HexUtils
import java.io.File
import java.nio.charset.Charset

object Sha256Utils {
    fun digest(bytes: ByteArray): ByteArray = digestBytes("SHA-256", bytes)

    fun digest(
        text: String,
        charset: Charset = Charsets.UTF_8,
    ): ByteArray = digest(text.toByteArray(charset))

    fun digest(file: File): ByteArray = digestFile("SHA-256", file)

    fun lowercaseHex(bytes: ByteArray): String = HexUtils.lowercase(digest(bytes))

    fun lowercaseHex(
        text: String,
        charset: Charset = Charsets.UTF_8,
    ): String = HexUtils.lowercase(digest(text, charset))

    fun lowercaseHex(file: File): String = HexUtils.lowercase(digest(file))
}
