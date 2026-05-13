package com.georgebindragon.android.core.settings

import kotlinx.coroutines.flow.StateFlow

interface ThemeSettingsRepository {
    val themeMode: StateFlow<ThemeMode>
    val appScale: StateFlow<AppScale>

    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setAppScale(scale: AppScale)
}
