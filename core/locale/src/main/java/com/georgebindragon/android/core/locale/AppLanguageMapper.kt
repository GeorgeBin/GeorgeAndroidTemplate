package com.georgebindragon.android.core.locale

import androidx.core.os.LocaleListCompat
import java.util.Locale

internal fun AppLanguage.toLocaleListCompat(): LocaleListCompat {
    return localeTag?.let(LocaleListCompat::forLanguageTags) ?: LocaleListCompat.getEmptyLocaleList()
}

internal fun LocaleListCompat.toAppLanguage(): AppLanguage {
    val tag = get(0)?.toLanguageTag() ?: return AppLanguage.System
    return AppLanguage.entries.firstOrNull { language ->
        language.localeTag?.matchesLanguageTag(tag) == true
    } ?: AppLanguage.System
}

private fun String.matchesLanguageTag(other: String): Boolean {
    val expected = Locale.forLanguageTag(this)
    val actual = Locale.forLanguageTag(other)
    return expected.language == actual.language &&
        (expected.script.isBlank() || actual.script.isBlank() || expected.script == actual.script)
}
