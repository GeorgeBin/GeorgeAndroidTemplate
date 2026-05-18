package com.georgebindragon.android.base.io

import org.junit.Assert.assertEquals
import org.junit.Test

class FileSizeFormatUtilsTest {
    @Test
    fun formatUsesBinaryUnits() {
        assertEquals("0 B", FileSizeFormatUtils.format(0L))
        assertEquals("1023 B", FileSizeFormatUtils.format(1023L))
        assertEquals("1 KiB", FileSizeFormatUtils.format(1024L))
        assertEquals("1.5 KiB", FileSizeFormatUtils.format(1536L))
        assertEquals("1 MiB", FileSizeFormatUtils.format(1024L * 1024L))
    }

    @Test
    fun formatSupportsNegativeValues() {
        assertEquals("-1.5 KiB", FileSizeFormatUtils.format(-1536L))
    }
}
