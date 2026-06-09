package com.georgebindragon.android.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.georgebindragon.android.core.appconfig.TabConfig
import com.georgebindragon.android.core.navigation.MainRoute

fun NavGraphBuilder.mainShellScreen(
    tabs: List<TabConfig>,
    selectedRoute: String?,
    appName: String,
    packageName: String,
    versionName: String,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    composable(MainRoute.Shell) {
        MainShellRoute(
            tabs = tabs,
            selectedRoute = selectedRoute,
            appName = appName,
            packageName = packageName,
            versionName = versionName,
            onTabClick = onTabClick,
            modifier = modifier,
            content = content,
        )
    }
}
