package com.georgebindragon.android.core.locale

import androidx.core.os.LocaleListCompat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppLanguageMapperTest {
    @Test
    fun systemMapsToEmptyLocaleList() {
        val localeList = AppLanguage.System.toLocaleListCompat()

        assertTrue(localeList.isEmpty)
    }

    @Test
    fun emptyLocaleListMapsToSystem() {
        val language = LocaleListCompat.getEmptyLocaleList().toAppLanguage()

        assertEquals(AppLanguage.System, language)
    }

    @Test
    fun supportedTagsMapToLanguages() {
        assertEquals(
            AppLanguage.SimplifiedChinese,
            LocaleListCompat.forLanguageTags("zh-Hans").toAppLanguage(),
        )
        assertEquals(
            AppLanguage.English,
            LocaleListCompat.forLanguageTags("en").toAppLanguage(),
        )
    }

    @Test
    fun unknownTagMapsToSystem() {
        val language = LocaleListCompat.forLanguageTags("fr").toAppLanguage()

        assertEquals(AppLanguage.System, language)
    }
}

