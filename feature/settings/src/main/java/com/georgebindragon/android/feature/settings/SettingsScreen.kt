package com.georgebindragon.android.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.designsystem.theme.TemplateTheme
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.ThemeMode

@Composable
internal fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    appScale: AppScale,
    onAppScaleChange: (AppScale) -> Unit,
    onBackHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
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
        Text(
            text = stringResource(R.string.settings_theme_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        ThemeMode.entries.forEach { option ->
            SettingsOptionRow(
                label = option.label(),
                selected = option == themeMode,
                onClick = { onThemeModeChange(option) },
            )
        }
        Text(
            text = stringResource(R.string.settings_app_scale_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        AppScale.entries.forEach { option ->
            SettingsOptionRow(
                label = option.label(),
                selected = option == appScale,
                onClick = { onAppScaleChange(option) },
            )
        }
        Button(onClick = onBackHomeClick) {
            Text(text = stringResource(R.string.settings_back_home))
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

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = dimensions.settingsRowElevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensions.settingsRowHorizontalPadding,
                    vertical = dimensions.settingsRowVerticalPadding,
                ),
            horizontalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
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

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    TemplateTheme {
        SettingsScreen(
            themeMode = ThemeMode.System,
            onThemeModeChange = {},
            appScale = AppScale.Standard,
            onAppScaleChange = {},
            onBackHomeClick = {},
        )
    }
}
