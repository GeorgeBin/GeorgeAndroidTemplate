package com.georgebindragon.android.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.locale.AppLanguage
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.PageOrientation
import com.georgebindragon.android.core.settings.ThemeMode
import com.georgebindragon.android.feature.home.HomeRoute
import com.georgebindragon.android.feature.settings.SettingsRoute
import com.georgebindragon.android.core.ui.component.FocusableTextButton
import com.georgebindragon.android.core.ui.focus.ProvideAppInteractionMode

@Composable
fun TemplateApp(
    appName: String,
    packageName: String,
    versionName: String,
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
    onExitClick: () -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentScreen by rememberSaveable { mutableStateOf(TemplateScreen.Home) }

    ProvideAppInteractionMode(mode = interactionMode) {
        Scaffold(
            modifier = modifier,
            bottomBar = {
                AppFooter(
                    appName = appName,
                    packageName = packageName,
                    versionName = versionName,
                    currentScreen = currentScreen,
                    onScreenChange = { currentScreen = it },
                )
            },
        ) { innerPadding ->
            TemplateNavHost(
                currentScreen = currentScreen,
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
                onBackHomeClick = { currentScreen = TemplateScreen.Home },
                onExitClick = onExitClick,
                onRestartClick = onRestartClick,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun TemplateNavHost(
    currentScreen: TemplateScreen,
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
    onExitClick: () -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (currentScreen) {
        TemplateScreen.Home -> HomeRoute(
            onExitClick = onExitClick,
            onRestartClick = onRestartClick,
            modifier = modifier,
        )

        TemplateScreen.Settings -> SettingsRoute(
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
            modifier = modifier,
        )
    }
}

@Composable
private fun AppFooter(
    appName: String,
    packageName: String,
    versionName: String,
    currentScreen: TemplateScreen,
    onScreenChange: (TemplateScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = dimensions.footerElevation,
        shadowElevation = dimensions.footerElevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.footerNavHorizontalPadding,
                        vertical = dimensions.footerVerticalPadding,
                    ),
            ) {
                TemplateScreen.entries.forEach { screen ->
                    FocusableTextButton(
                        onClick = { onScreenChange(screen) },
                        enabled = screen != currentScreen,
                    ) {
                        Text(text = screen.label())
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.footerInfoHorizontalPadding,
                        vertical = dimensions.footerVerticalPadding,
                    ),
            ) {
                Text(
                    text = appName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "$packageName  v$versionName",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Composable
private fun TemplateScreen.label(): String = when (this) {
    TemplateScreen.Home -> stringResource(R.string.nav_home)
    TemplateScreen.Settings -> stringResource(R.string.nav_settings)
}

private enum class TemplateScreen {
    Home,
    Settings,
}
