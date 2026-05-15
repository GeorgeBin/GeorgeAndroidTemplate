package com.georgebindragon.android.core.locale

class SetAppLanguageUseCase(
    private val languageManager: LanguageManager,
) {
    operator fun invoke(language: AppLanguage) {
        languageManager.setLanguage(language)
    }
}

