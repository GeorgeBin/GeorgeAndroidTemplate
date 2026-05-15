package com.georgebindragon.android.core.settings

import kotlinx.coroutines.flow.StateFlow

interface ThemeSettingsRepository {
    val themeMode: StateFlow<ThemeMode>
    val appScale: StateFlow<AppScale>
    val pageOrientation: StateFlow<PageOrientation>
    val expertMode: StateFlow<Boolean>

    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setAppScale(scale: AppScale)
    suspend fun setPageOrientation(orientation: PageOrientation)
    suspend fun setExpertMode(enabled: Boolean)
}
