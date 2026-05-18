package com.georgebindragon.android.base.io

import org.junit.Assert.assertEquals
import org.junit.Test

class FileNameUtilsTest {
    @Test
    fun sanitizeReplacesInvalidCharactersAndControls() {
        assertEquals("a_b_c_d_e_f_g_h_i", FileNameUtils.sanitize("a<b>c:d\"e/f\\g|h?i"))
        assertEquals("line_break", FileNameUtils.sanitize("line\nbreak"))
    }

    @Test
    fun sanitizeTrimsWhitespaceAndReplacementCharacters() {
        assertEquals("report", FileNameUtils.sanitize("  report  "))
        assertEquals("report", FileNameUtils.sanitize("///report???"))
    }

    @Test
    fun sanitizeFallsBackForBlankOrAllInvalidInput() {
        assertEquals("untitled", FileNameUtils.sanitize("   "))
        assertEquals("untitled", FileNameUtils.sanitize("////"))
    }
}
