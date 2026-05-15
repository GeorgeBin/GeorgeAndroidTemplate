package com.georgebindragon.android.core.locale

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

class SetAppLanguageUseCaseTest {
    @Test
    fun delegatesLanguageChangeToManager() {
        val manager = FakeLanguageManager()
        val useCase = SetAppLanguageUseCase(manager)

        useCase(AppLanguage.English)

        assertEquals(AppLanguage.English, manager.currentLanguage.value)
    }
}

private class FakeLanguageManager : LanguageManager {
    override val supportedLanguages: List<AppLanguage> = AppLanguage.entries
    private val mutableCurrentLanguage = MutableStateFlow(AppLanguage.System)
    override val currentLanguage: StateFlow<AppLanguage> = mutableCurrentLanguage

    override fun refreshLanguage() = Unit

    override fun setLanguage(language: AppLanguage) {
        mutableCurrentLanguage.value = language
    }
}
