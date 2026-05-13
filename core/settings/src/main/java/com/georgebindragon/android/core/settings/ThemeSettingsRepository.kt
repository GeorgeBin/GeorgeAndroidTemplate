package com.georgebindragon.android.core.settings

import kotlinx.coroutines.flow.StateFlow

interface ThemeSettingsRepository {
    val themeMode: StateFlow<ThemeMode>
    val appScale: StateFlow<AppScale>

    fun setThemeMode(mode: ThemeMode)
    fun setAppScale(scale: AppScale)
}
