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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.georgebindragon.android.core.appconfig.AppConfigProvider
import com.georgebindragon.android.core.auth.AuthRepository
import com.georgebindragon.android.core.designsystem.theme.TemplateAppScale
import com.georgebindragon.android.core.designsystem.systembar.enableImmersiveStatusBar
import com.georgebindragon.android.core.designsystem.theme.TemplateTheme
import com.georgebindragon.android.core.designsystem.theme.TemplateThemeMode
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.input.focus.InteractionModeManager
import com.georgebindragon.android.core.input.focus.SetInteractionModeUseCase
import com.georgebindragon.android.core.locale.LanguageManager
import com.georgebindragon.android.core.locale.SetAppLanguageUseCase
import com.georgebindragon.android.core.permission.PermissionIntentFactory
import com.georgebindragon.android.core.permission.PermissionRepository
import com.georgebindragon.android.core.privacy.PrivacyRepository
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.PageOrientation
import com.georgebindragon.android.core.settings.ThemeSettingsRepository
import com.georgebindragon.android.core.settings.ThemeMode
import com.georgebindragon.android.core.startup.StartupCoordinator
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appConfigProvider: AppConfigProvider

    @Inject
    lateinit var startupCoordinator: StartupCoordinator

    @Inject
    lateinit var privacyRepository: PrivacyRepository

    @Inject
    lateinit var permissionRepository: PermissionRepository

    @Inject
    lateinit var permissionIntentFactory: PermissionIntentFactory

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var themeSettingsRepository: ThemeSettingsRepository

    @Inject
    lateinit var interactionModeManager: InteractionModeManager

    @Inject
    lateinit var setInteractionModeUseCase: SetInteractionModeUseCase

    @Inject
    lateinit var languageManager: LanguageManager

    @Inject
    lateinit var setAppLanguageUseCase: SetAppLanguageUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableImmersiveStatusBar()
        languageManager.refreshLanguage()
        val packageName = applicationContext.packageName
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val themeMode by themeSettingsRepository.themeMode.collectAsState()
            val appScale by themeSettingsRepository.appScale.collectAsState()
            val pageOrientation by themeSettingsRepository.pageOrientation.collectAsState()
            val expertMode by themeSettingsRepository.expertMode.collectAsState()
            val interactionMode by interactionModeManager.interactionMode.collectAsState()
            val language by languageManager.currentLanguage.collectAsState()

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
                            themeSettingsRepository.setThemeMode(mode)
                        }
                    },
                    appScale = appScale,
                    onAppScaleChange = { scale ->
                        coroutineScope.launch {
                            themeSettingsRepository.setAppScale(scale)
                        }
                    },
                    pageOrientation = pageOrientation,
                    onPageOrientationChange = { orientation ->
                        coroutineScope.launch {
                            themeSettingsRepository.setPageOrientation(orientation)
                        }
                    },
                    expertMode = expertMode,
                    onExpertModeChange = { enabled ->
                        coroutineScope.launch {
                            themeSettingsRepository.setExpertMode(enabled)
                        }
                    },
                    interactionMode = interactionMode,
                    onInteractionModeChange = { mode ->
                        coroutineScope.launch {
                            setInteractionModeUseCase(mode)
                        }
                    },
                    supportedLanguages = languageManager.supportedLanguages,
                    language = language,
                    onLanguageChange = { language ->
                        setAppLanguageUseCase(language)
                    },
                    onExitClick = { finishAffinity() },
                    onRestartClick = { ProcessPhoenix.triggerRebirth(this) },
                    modifier = Modifier.fillMaxSize(),
                    appConfig = appConfigProvider.getConfig(),
                    startupCoordinator = startupCoordinator,
                    privacyRepository = privacyRepository,
                    permissionRepository = permissionRepository,
                    permissionIntentFactory = permissionIntentFactory,
                    authRepository = authRepository,
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
