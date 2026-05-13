package com.georgebindragon.android.app

import com.georgebindragon.android.core.settings.InMemoryThemeSettingsRepository
import com.georgebindragon.android.core.settings.ThemeSettingsRepository

object AppDependencies {
    val themeSettingsRepository: ThemeSettingsRepository = InMemoryThemeSettingsRepository()
}
