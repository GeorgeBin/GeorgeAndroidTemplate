package com.georgebindragon.android.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.ThemeMode

@Composable
fun SettingsRoute(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    appScale: AppScale,
    onAppScaleChange: (AppScale) -> Unit,
    onBackHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsScreen(
        themeMode = themeMode,
        onThemeModeChange = onThemeModeChange,
        appScale = appScale,
        onAppScaleChange = onAppScaleChange,
        onBackHomeClick = onBackHomeClick,
        modifier = modifier,
    )
}
