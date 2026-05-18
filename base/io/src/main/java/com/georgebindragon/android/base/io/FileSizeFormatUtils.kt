package com.georgebindragon.android.base.io

import java.util.Locale
import kotlin.math.abs

object FileSizeFormatUtils {
    private const val UNIT = 1024.0
    private val units = listOf("B", "KiB", "MiB", "GiB", "TiB")

    fun format(bytes: Long): String {
        if (abs(bytes) < UNIT) return "$bytes B"

        var value = bytes.toDouble()
        var unitIndex = 0
        while (abs(value) >= UNIT && unitIndex < units.lastIndex) {
            value /= UNIT
            unitIndex += 1
        }

        val number = if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else {
            String.format(Locale.US, "%.1f", value)
        }
        return "$number ${units[unitIndex]}"
    }
}
