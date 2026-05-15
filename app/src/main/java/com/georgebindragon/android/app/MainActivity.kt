package com.georgebindragon.android.app

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.Surface
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.georgebindragon.android.core.designsystem.theme.TemplateAppScale
import com.georgebindragon.android.core.designsystem.systembar.enableImmersiveStatusBar
import com.georgebindragon.android.core.designsystem.theme.TemplateTheme
import com.georgebindragon.android.core.designsystem.theme.TemplateThemeMode
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.PageOrientation
import com.georgebindragon.android.core.settings.ThemeMode
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableImmersiveStatusBar()
        AppDependencies.languageManager.refreshLanguage()
        val packageName = applicationContext.packageName
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val themeMode by AppDependencies.themeSettingsRepository.themeMode.collectAsState()
            val appScale by AppDependencies.themeSettingsRepository.appScale.collectAsState()
            val pageOrientation by AppDependencies.themeSettingsRepository.pageOrientation.collectAsState()
            val expertMode by AppDependencies.themeSettingsRepository.expertMode.collectAsState()
            val interactionMode by AppDependencies.interactionModeManager.interactionMode.collectAsState()
            val language by AppDependencies.languageManager.currentLanguage.collectAsState()

            LaunchedEffect(pageOrientation) {
                requestedOrientation = toRequestedOrientation(pageOrientation)
            }

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
                    pageOrientation = pageOrientation,
                    onPageOrientationChange = { orientation ->
                        coroutineScope.launch {
                            AppDependencies.themeSettingsRepository.setPageOrientation(orientation)
                        }
                    },
                    expertMode = expertMode,
                    onExpertModeChange = { enabled ->
                        coroutineScope.launch {
                            AppDependencies.themeSettingsRepository.setExpertMode(enabled)
                        }
                    },
                    interactionMode = interactionMode,
                    onInteractionModeChange = { mode ->
                        coroutineScope.launch {
                            AppDependencies.setInteractionModeUseCase(mode)
                        }
                    },
                    supportedLanguages = AppDependencies.languageManager.supportedLanguages,
                    language = language,
                    onLanguageChange = { language ->
                        AppDependencies.setAppLanguageUseCase(language)
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

private fun MainActivity.toNaturalOrientation(): NaturalOrientation {
    val orientation = resources.configuration.orientation
    return when (currentDisplayRotation()) {
        Surface.ROTATION_0,
        Surface.ROTATION_180 -> if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            NaturalOrientation.Landscape
        } else {
            NaturalOrientation.Portrait
        }

        Surface.ROTATION_90,
        Surface.ROTATION_270 -> if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            NaturalOrientation.Portrait
        } else {
            NaturalOrientation.Landscape
        }

        else -> NaturalOrientation.Portrait
    }
}

@Suppress("DEPRECATION")
private fun MainActivity.currentDisplayRotation(): Int {
    return windowManager.defaultDisplay.rotation
}

private fun MainActivity.toRequestedOrientation(pageOrientation: PageOrientation): Int {
    if (pageOrientation == PageOrientation.System) {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    return when (toNaturalOrientation()) {
        NaturalOrientation.Portrait -> when (pageOrientation) {
            PageOrientation.System -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            PageOrientation.Rotation0 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            PageOrientation.Rotation90 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            PageOrientation.Rotation180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            PageOrientation.Rotation270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        }

        NaturalOrientation.Landscape -> when (pageOrientation) {
            PageOrientation.System -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            PageOrientation.Rotation0 -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            PageOrientation.Rotation90 -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            PageOrientation.Rotation180 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            PageOrientation.Rotation270 -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
        }
    }
}

private enum class NaturalOrientation {
    Portrait,
    Landscape,
}
