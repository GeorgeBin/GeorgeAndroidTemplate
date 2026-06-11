package com.georgebindragon.android.feature.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.georgebindragon.android.core.appconfig.PermissionFeatureConfig
import com.georgebindragon.android.core.permission.PermissionRepository
import kotlinx.coroutines.launch

@Composable
fun PermissionOverviewRoute(
    permissionConfig: PermissionFeatureConfig,
    permissionRepository: PermissionRepository,
    onContinueRequest: () -> Unit,
    onComplete: () -> Unit,
) {
    val viewModel = remember(permissionRepository) {
        PermissionViewModel(permissionRepository)
    }
    val gateState by viewModel.gateState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(permissionConfig) {
        viewModel.refresh(permissionConfig.declarations)
    }

    PermissionOverviewScreen(
        gateState = gateState,
        allowSkipOptional = permissionConfig.allowSkipOptional,
        onContinueClick = {
            scope.launch {
                val refreshed = permissionRepository.refresh(permissionConfig.declarations)
                viewModel.markOverviewSeen(permissionConfig.overviewVersion)
                if (refreshed.hasMissingPermissions) {
                    onContinueRequest()
                } else {
                    onComplete()
                }
            }
        },
        onSkipOptionalClick = {
            scope.launch {
                viewModel.skipOptionalPermissions(
                    gateState.missingOptional.map { it.declaration.permission },
                )
                viewModel.markOverviewSeen(permissionConfig.overviewVersion)
                onComplete()
            }
        },
    )
}
