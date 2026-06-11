package com.georgebindragon.android.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.locale.AppLanguage
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.PageOrientation
import com.georgebindragon.android.core.settings.ThemeMode

@Composable
fun SettingsRoute(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    appScale: AppScale,
    onAppScaleChange: (AppScale) -> Unit,
    pageOrientation: PageOrientation,
    onPageOrientationChange: (PageOrientation) -> Unit,
    expertMode: Boolean,
    onExpertModeChange: (Boolean) -> Unit,
    interactionMode: AppInteractionMode,
    onInteractionModeChange: (AppInteractionMode) -> Unit,
    supportedLanguages: List<AppLanguage>,
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onBackHomeClick: () -> Unit,
    onPermissionClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsScreen(
        themeMode = themeMode,
        onThemeModeChange = onThemeModeChange,
        appScale = appScale,
        onAppScaleChange = onAppScaleChange,
        pageOrientation = pageOrientation,
        onPageOrientationChange = onPageOrientationChange,
        expertMode = expertMode,
        onExpertModeChange = onExpertModeChange,
        interactionMode = interactionMode,
        onInteractionModeChange = onInteractionModeChange,
        supportedLanguages = supportedLanguages,
        language = language,
        onLanguageChange = onLanguageChange,
        onBackHomeClick = onBackHomeClick,
        onPermissionClick = onPermissionClick,
        onLogoutClick = onLogoutClick,
        modifier = modifier,
    )
}
