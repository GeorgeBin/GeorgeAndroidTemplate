package com.georgebindragon.android.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.georgebindragon.android.core.navigation.MainRoute

fun NavGraphBuilder.homeScreen(
    onExitClick: () -> Unit,
    onRestartClick: () -> Unit,
) {
    composable(MainRoute.Home) {
        HomeRoute(
            onExitClick = onExitClick,
            onRestartClick = onRestartClick,
        )
    }
}
