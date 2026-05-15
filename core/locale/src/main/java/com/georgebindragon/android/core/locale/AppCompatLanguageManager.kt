package com.georgebindragon.android.core.locale

import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppCompatLanguageManager : LanguageManager {
    override val supportedLanguages: List<AppLanguage> = AppLanguage.entries

    private val mutableCurrentLanguage = MutableStateFlow(readCurrentLanguage())
    override val currentLanguage: StateFlow<AppLanguage> = mutableCurrentLanguage.asStateFlow()

    override fun refreshLanguage() {
        mutableCurrentLanguage.value = readCurrentLanguage()
    }

    override fun setLanguage(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(language.toLocaleListCompat())
        mutableCurrentLanguage.value = language
    }

    private fun readCurrentLanguage(): AppLanguage {
        return AppCompatDelegate.getApplicationLocales().toAppLanguage()
    }
}

