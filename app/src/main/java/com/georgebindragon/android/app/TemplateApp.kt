package com.georgebindragon.android.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.georgebindragon.android.core.appconfig.AppConfig
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.locale.AppLanguage
import com.georgebindragon.android.core.navigation.MainRoute
import com.georgebindragon.android.core.navigation.RootRoute
import com.georgebindragon.android.core.navigation.StartupRoute as StartupNavigationRoute
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.PageOrientation
import com.georgebindragon.android.core.settings.ThemeMode
import com.georgebindragon.android.core.startup.StartupDestination
import com.georgebindragon.android.core.ui.focus.ProvideAppInteractionMode
import com.georgebindragon.android.feature.home.homeScreen
import com.georgebindragon.android.feature.main.MainShellRoute
import com.georgebindragon.android.feature.privacy.privacyScreen
import com.georgebindragon.android.feature.settings.settingsScreen

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
    appConfig: AppConfig = AppDependencies.appConfigProvider.getConfig(),
) {
    val rootNavController = rememberNavController()

    ProvideAppInteractionMode(mode = interactionMode) {
        NavHost(
            navController = rootNavController,
            startDestination = RootRoute.Startup,
            modifier = modifier,
        ) {
            composable(RootRoute.Startup) {
                StartupRoute(
                    startupCoordinator = AppDependencies.startupCoordinator,
                    onDestinationResolved = { destination ->
                        rootNavController.navigate(destination.toRoute()) {
                            popUpTo(RootRoute.Startup) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                )
            }
            privacyScreen(
                privacyConfig = appConfig.privacy,
                privacyRepository = AppDependencies.privacyRepository,
                onAccepted = {
                    rootNavController.navigate(RootRoute.Main) {
                        popUpTo(StartupNavigationRoute.Privacy) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onRejected = onExitClick,
            )
            composable(StartupNavigationRoute.PermissionOverview) {
                Text(text = "权限总览")
            }
            composable(StartupNavigationRoute.PermissionRequest) {
                Text(text = "权限申请")
            }
            composable(StartupNavigationRoute.Login) {
                Text(text = "登录")
            }
            composable(RootRoute.Main) {
                MainGraph(
                    appConfig = appConfig,
                    appName = appName,
                    packageName = packageName,
                    versionName = versionName,
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
                    onExitClick = onExitClick,
                    onRestartClick = onRestartClick,
                )
            }
        }
    }
}

private fun StartupDestination.toRoute(): String = when (this) {
    StartupDestination.Privacy -> StartupNavigationRoute.Privacy
    StartupDestination.PermissionOverview -> StartupNavigationRoute.PermissionOverview
    StartupDestination.Login -> StartupNavigationRoute.Login
    StartupDestination.Main -> RootRoute.Main
}

@Composable
private fun MainGraph(
    appConfig: AppConfig,
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
) {
    val mainNavController = rememberNavController()
    val visibleTabs = remember(appConfig.main.tabs) {
        appConfig.main.tabs
            .filter { it.visible }
            .sortedBy { it.order }
    }
    val startRoute = visibleTabs.firstOrNull()?.route ?: MainRoute.Home
    val backStackEntry by mainNavController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route ?: startRoute

    MainShellRoute(
        tabs = visibleTabs,
        selectedRoute = selectedRoute,
        appName = appName,
        packageName = packageName,
        versionName = versionName,
        onTabClick = { route ->
            mainNavController.navigate(route) {
                popUpTo(mainNavController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        NavHost(
            navController = mainNavController,
            startDestination = startRoute,
            modifier = Modifier.fillMaxSize(),
        ) {
            homeScreen(
                onExitClick = onExitClick,
                onRestartClick = onRestartClick,
            )
            settingsScreen(
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
                onBackHomeClick = {
                    mainNavController.navigate(MainRoute.Home) {
                        popUpTo(mainNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}
