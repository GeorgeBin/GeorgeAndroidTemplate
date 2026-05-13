package com.georgebindragon.android.core.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryThemeSettingsRepository(
    initialThemeMode: ThemeMode = ThemeMode.System,
    initialAppScale: AppScale = AppScale.Standard,
) : ThemeSettingsRepository {
    private val mutableThemeMode = MutableStateFlow(initialThemeMode)
    private val mutableAppScale = MutableStateFlow(initialAppScale)

    override val themeMode: StateFlow<ThemeMode> = mutableThemeMode.asStateFlow()
    override val appScale: StateFlow<AppScale> = mutableAppScale.asStateFlow()

    override fun setThemeMode(mode: ThemeMode) {
        mutableThemeMode.value = mode
    }

    override fun setAppScale(scale: AppScale) {
        mutableAppScale.value = scale
    }
}
