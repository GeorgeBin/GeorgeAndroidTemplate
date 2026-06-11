package com.georgebindragon.android.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.georgebindragon.android.core.appconfig.AppConfig
import com.georgebindragon.android.core.auth.AuthRepository
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.locale.AppLanguage
import com.georgebindragon.android.core.navigation.MainRoute
import com.georgebindragon.android.core.navigation.RootRoute
import com.georgebindragon.android.core.navigation.StartupRoute as StartupNavigationRoute
import com.georgebindragon.android.core.permission.PermissionIntentFactory
import com.georgebindragon.android.core.permission.PermissionRepository
import com.georgebindragon.android.core.privacy.PrivacyRepository
import com.georgebindragon.android.core.settings.AppScale
import com.georgebindragon.android.core.settings.PageOrientation
import com.georgebindragon.android.core.settings.ThemeMode
import com.georgebindragon.android.core.startup.StartupDestination
import com.georgebindragon.android.core.startup.StartupCoordinator
import com.georgebindragon.android.core.ui.focus.ProvideAppInteractionMode
import com.georgebindragon.android.feature.auth.loginScreen
import com.georgebindragon.android.feature.home.homeScreen
import com.georgebindragon.android.feature.main.MainShellRoute
import com.georgebindragon.android.feature.permission.permissionOverviewScreen
import com.georgebindragon.android.feature.permission.permissionRequestScreen
import com.georgebindragon.android.feature.privacy.privacyScreen
import com.georgebindragon.android.feature.settings.settingsScreen
import kotlinx.coroutines.launch

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
    appConfig: AppConfig,
    startupCoordinator: StartupCoordinator,
    privacyRepository: PrivacyRepository,
    permissionRepository: PermissionRepository,
    permissionIntentFactory: PermissionIntentFactory,
    authRepository: AuthRepository,
) {
    val rootNavController = rememberNavController()
    val scope = rememberCoroutineScope()

    ProvideAppInteractionMode(mode = interactionMode) {
        NavHost(
            navController = rootNavController,
            startDestination = RootRoute.Startup,
            modifier = modifier,
        ) {
            composable(RootRoute.Startup) {
                StartupRoute(
                    startupCoordinator = startupCoordinator,
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
                privacyRepository = privacyRepository,
                onAccepted = {
                    rootNavController.navigate(RootRoute.Startup) {
                        popUpTo(StartupNavigationRoute.Privacy) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onRejected = onExitClick,
            )
            permissionOverviewScreen(
                permissionConfig = appConfig.permission,
                permissionRepository = permissionRepository,
                onContinueRequest = {
                    rootNavController.navigate(StartupNavigationRoute.PermissionRequest) {
                        launchSingleTop = true
                    }
                },
                onComplete = {
                    rootNavController.navigate(RootRoute.Startup) {
                        popUpTo(StartupNavigationRoute.PermissionOverview) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
            permissionRequestScreen(
                permissionConfig = appConfig.permission,
                permissionRepository = permissionRepository,
                permissionIntentFactory = permissionIntentFactory,
                onComplete = {
                    rootNavController.navigate(RootRoute.Startup) {
                        popUpTo(StartupNavigationRoute.PermissionOverview) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
            loginScreen(
                authConfig = appConfig.auth,
                authRepository = authRepository,
                onLoginSuccess = {
                    rootNavController.navigate(RootRoute.Main) {
                        popUpTo(StartupNavigationRoute.Login) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
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
                    onPermissionClick = {
                        rootNavController.navigate(StartupNavigationRoute.PermissionOverview) {
                            launchSingleTop = true
                        }
                    },
                    onPrivacyClick = {
                        rootNavController.navigate(StartupNavigationRoute.Privacy) {
                            launchSingleTop = true
                        }
                    },
                    onLogoutClick = {
                        scope.launch {
                            authRepository.logout()
                            rootNavController.navigate(RootRoute.Startup) {
                                popUpTo(RootRoute.Main) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
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
    onPermissionClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit,
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
                settingsConfig = appConfig.settings,
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
                onPermissionClick = onPermissionClick,
                onPrivacyClick = onPrivacyClick,
                onAboutClick = {},
                onLogoutClick = onLogoutClick,
                onRestartClick = onRestartClick,
            )
        }
    }
}
