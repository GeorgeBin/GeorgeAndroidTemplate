package com.georgebindragon.android.base.security.hex

object HexUtils {
    private val lowercaseDigits = "0123456789abcdef".toCharArray()

    fun lowercase(bytes: ByteArray): String {
        val chars = CharArray(bytes.size * 2)
        bytes.forEachIndexed { index, byte ->
            val value = byte.toInt() and 0xff
            chars[index * 2] = lowercaseDigits[value ushr 4]
            chars[index * 2 + 1] = lowercaseDigits[value and 0x0f]
        }
        return String(chars)
    }
}
