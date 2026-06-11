package com.georgebindragon.android.feature.permission

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.georgebindragon.android.core.appconfig.PermissionFeatureConfig
import com.georgebindragon.android.core.navigation.StartupRoute
import com.georgebindragon.android.core.permission.PermissionIntentFactory
import com.georgebindragon.android.core.permission.PermissionRepository

fun NavGraphBuilder.permissionOverviewScreen(
    permissionConfig: PermissionFeatureConfig,
    permissionRepository: PermissionRepository,
    onContinueRequest: () -> Unit,
    onComplete: () -> Unit,
) {
    composable(StartupRoute.PermissionOverview) {
        PermissionOverviewRoute(
            permissionConfig = permissionConfig,
            permissionRepository = permissionRepository,
            onContinueRequest = onContinueRequest,
            onComplete = onComplete,
        )
    }
}

fun NavGraphBuilder.permissionRequestScreen(
    permissionConfig: PermissionFeatureConfig,
    permissionRepository: PermissionRepository,
    permissionIntentFactory: PermissionIntentFactory,
    onComplete: () -> Unit,
) {
    composable(StartupRoute.PermissionRequest) {
        PermissionRequestRoute(
            permissionConfig = permissionConfig,
            permissionRepository = permissionRepository,
            permissionIntentFactory = permissionIntentFactory,
            onComplete = onComplete,
        )
    }
}
