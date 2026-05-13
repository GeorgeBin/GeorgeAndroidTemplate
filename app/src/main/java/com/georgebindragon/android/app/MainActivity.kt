package com.georgebindragon.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.georgebindragon.android.core.designsystem.theme.TemplateAppScale
import com.georgebindragon.android.core.designsystem.systembar.enableImmersiveStatusBar
import com.georgebindragon.android.core.designsystem.theme.TemplateTheme
import com.georgebindragon.android.core.designsystem.theme.TemplateThemeMode
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.ThemeMode
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableImmersiveStatusBar()
        val packageName = applicationContext.packageName
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val themeMode by AppDependencies.themeSettingsRepository.themeMode.collectAsState()
            val appScale by AppDependencies.themeSettingsRepository.appScale.collectAsState()

            TemplateTheme(
                themeMode = themeMode.toTemplateThemeMode(),
                appScale = appScale.toTemplateAppScale(),
            ) {
                TemplateApp(
                    appName = stringResource(R.string.app_name),
                    packageName = packageName,
                    versionName = versionName,
                    themeMode = themeMode,
                    onThemeModeChange = { mode ->
                        coroutineScope.launch {
                            AppDependencies.themeSettingsRepository.setThemeMode(mode)
                        }
                    },
                    appScale = appScale,
                    onAppScaleChange = { scale ->
                        coroutineScope.launch {
                            AppDependencies.themeSettingsRepository.setAppScale(scale)
                        }
                    },
                    onExitClick = { finishAffinity() },
                    onRestartClick = { ProcessPhoenix.triggerRebirth(this) },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

private fun ThemeMode.toTemplateThemeMode(): TemplateThemeMode = when (this) {
    ThemeMode.System -> TemplateThemeMode.System
    ThemeMode.Light -> TemplateThemeMode.Light
    ThemeMode.Dark -> TemplateThemeMode.Dark
}

private fun AppScale.toTemplateAppScale(): TemplateAppScale = when (this) {
    AppScale.Small -> TemplateAppScale.Small
    AppScale.Standard -> TemplateAppScale.Standard
    AppScale.Large -> TemplateAppScale.Large
    AppScale.ExtraLarge -> TemplateAppScale.ExtraLarge
}
