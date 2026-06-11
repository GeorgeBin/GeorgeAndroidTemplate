package com.georgebindragon.android.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.georgebindragon.android.core.appconfig.SettingsFeatureConfig
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.designsystem.theme.TemplateTheme
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.locale.AppLanguage
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.PageOrientation
import com.georgebindragon.android.core.settings.ThemeMode
import com.georgebindragon.android.core.ui.component.FocusableButton
import com.georgebindragon.android.core.ui.component.FocusableSurface
import com.georgebindragon.android.core.ui.focus.ProvideAppInteractionMode

@Composable
internal fun SettingsScreen(
    settingsConfig: SettingsFeatureConfig,
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
    onPrivacyClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = dimensions.screenHorizontalPadding,
                vertical = dimensions.screenVerticalPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        if (settingsConfig.showExpertMode) {
            SettingsSwitchRow(
                label = stringResource(R.string.settings_expert_mode_title),
                checked = expertMode,
                onCheckedChange = onExpertModeChange,
            )
        }
        if (settingsConfig.showTheme) {
            SettingsSectionTitle(text = stringResource(R.string.settings_theme_title))
            ThemeMode.entries.forEach { option ->
                SettingsOptionRow(
                    label = option.label(),
                    selected = option == themeMode,
                    onClick = { onThemeModeChange(option) },
                )
            }
        }
        if (settingsConfig.showAppScale) {
            SettingsSectionTitle(text = stringResource(R.string.settings_app_scale_title))
            AppScale.entries.forEach { option ->
                SettingsOptionRow(
                    label = option.label(),
                    selected = option == appScale,
                    onClick = { onAppScaleChange(option) },
                )
            }
        }
        if (settingsConfig.showPageOrientation) {
            SettingsSectionTitle(text = stringResource(R.string.settings_page_orientation_title))
            PageOrientation.entries.forEach { option ->
                SettingsOptionRow(
                    label = option.label(),
                    selected = option == pageOrientation,
                    onClick = { onPageOrientationChange(option) },
                )
            }
        }
        if (expertMode) {
            SettingsSectionTitle(text = stringResource(R.string.settings_interaction_mode_title))
            AppInteractionMode.entries.forEach { option ->
                SettingsOptionRow(
                    label = option.label(),
                    selected = option == interactionMode,
                    onClick = { onInteractionModeChange(option) },
                )
            }
        }
        if (settingsConfig.showLanguage) {
            SettingsSectionTitle(text = stringResource(R.string.settings_language_title))
            supportedLanguages.forEach { option ->
                SettingsOptionRow(
                    label = option.label(),
                    selected = option == language,
                    onClick = { onLanguageChange(option) },
                )
            }
        }
        if (settingsConfig.showPermission) {
            FocusableButton(onClick = onPermissionClick) {
                Text(text = stringResource(R.string.settings_permission_management))
            }
        }
        if (settingsConfig.showPrivacy) {
            FocusableButton(onClick = onPrivacyClick) {
                Text(text = stringResource(R.string.settings_privacy))
            }
        }
        if (settingsConfig.showAbout) {
            FocusableButton(onClick = onAboutClick) {
                Text(text = stringResource(R.string.settings_about))
            }
        }
        if (settingsConfig.showLogout) {
            FocusableButton(onClick = onLogoutClick) {
                Text(text = stringResource(R.string.settings_logout))
            }
        }
        FocusableButton(onClick = onRestartClick) {
            Text(text = stringResource(R.string.settings_restart))
        }
        FocusableButton(onClick = onBackHomeClick) {
            Text(text = stringResource(R.string.settings_back_home))
        }
    }
}

@Composable
private fun SettingsSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun SettingsSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    FocusableSurface(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = dimensions.settingsRowElevation,
        contentPadding = PaddingValues(
            horizontal = dimensions.settingsRowHorizontalPadding,
            vertical = dimensions.settingsRowVerticalPadding,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Switch(
                checked = checked,
                onCheckedChange = null,
            )
        }
    }
}

@Composable
private fun SettingsOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    FocusableSurface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = dimensions.settingsRowElevation,
        contentPadding = PaddingValues(
            horizontal = dimensions.settingsRowHorizontalPadding,
            vertical = dimensions.settingsRowVerticalPadding,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun ThemeMode.label(): String = when (this) {
    ThemeMode.System -> stringResource(R.string.settings_theme_system)
    ThemeMode.Light -> stringResource(R.string.settings_theme_light)
    ThemeMode.Dark -> stringResource(R.string.settings_theme_dark)
}

@Composable
private fun AppScale.label(): String = when (this) {
    AppScale.Small -> stringResource(R.string.settings_app_scale_small)
    AppScale.Standard -> stringResource(R.string.settings_app_scale_standard)
    AppScale.Large -> stringResource(R.string.settings_app_scale_large)
    AppScale.ExtraLarge -> stringResource(R.string.settings_app_scale_extra_large)
}

@Composable
private fun PageOrientation.label(): String = when (this) {
    PageOrientation.System -> stringResource(R.string.settings_page_orientation_system)
    PageOrientation.Rotation0 -> stringResource(R.string.settings_page_orientation_0)
    PageOrientation.Rotation90 -> stringResource(R.string.settings_page_orientation_90)
    PageOrientation.Rotation180 -> stringResource(R.string.settings_page_orientation_180)
    PageOrientation.Rotation270 -> stringResource(R.string.settings_page_orientation_270)
}

@Composable
private fun AppInteractionMode.label(): String = when (this) {
    AppInteractionMode.Auto -> stringResource(R.string.settings_interaction_mode_auto)
    AppInteractionMode.Touch -> stringResource(R.string.settings_interaction_mode_touch)
    AppInteractionMode.Remote -> stringResource(R.string.settings_interaction_mode_remote)
}

@Composable
private fun AppLanguage.label(): String = when (this) {
    AppLanguage.System -> stringResource(R.string.settings_language_system)
    AppLanguage.SimplifiedChinese -> stringResource(R.string.settings_language_simplified_chinese)
    AppLanguage.English -> stringResource(R.string.settings_language_english)
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    TemplateTheme {
        ProvideAppInteractionMode(mode = AppInteractionMode.Auto) {
            SettingsScreen(
                settingsConfig = SettingsFeatureConfig(),
                themeMode = ThemeMode.System,
                onThemeModeChange = {},
                appScale = AppScale.Standard,
                onAppScaleChange = {},
                pageOrientation = PageOrientation.System,
                onPageOrientationChange = {},
                expertMode = true,
                onExpertModeChange = {},
                interactionMode = AppInteractionMode.Auto,
                onInteractionModeChange = {},
                supportedLanguages = AppLanguage.entries,
                language = AppLanguage.System,
                onLanguageChange = {},
                onBackHomeClick = {},
                onPermissionClick = {},
                onPrivacyClick = {},
                onAboutClick = {},
                onLogoutClick = {},
                onRestartClick = {},
            )
        }
    }
}
