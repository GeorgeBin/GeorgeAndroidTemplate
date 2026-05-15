package com.georgebindragon.android.core.locale

import kotlinx.coroutines.flow.StateFlow

interface LanguageManager {
    val supportedLanguages: List<AppLanguage>
    val currentLanguage: StateFlow<AppLanguage>

    fun refreshLanguage()
    fun setLanguage(language: AppLanguage)
}

