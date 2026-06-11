package com.georgebindragon.android.feature.permission

import android.content.ActivityNotFoundException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.georgebindragon.android.core.appconfig.PermissionFeatureConfig
import com.georgebindragon.android.core.permission.PermissionIntentFactory
import com.georgebindragon.android.core.permission.PermissionRepository
import com.georgebindragon.android.core.permission.runtimePermissionName
import kotlinx.coroutines.launch

@Composable
fun PermissionRequestRoute(
    permissionConfig: PermissionFeatureConfig,
    permissionRepository: PermissionRepository,
    permissionIntentFactory: PermissionIntentFactory,
    onComplete: () -> Unit,
) {
    val viewModel = remember(permissionRepository) {
        PermissionViewModel(permissionRepository)
    }
    val gateState by viewModel.gateState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val runtimePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        scope.launch {
            val refreshed = permissionRepository.refresh(permissionConfig.declarations)
            if (refreshed.missingRequired.isEmpty() && refreshed.missingOptional.isEmpty()) {
                onComplete()
            }
        }
    }

    LaunchedEffect(permissionConfig) {
        viewModel.refresh(permissionConfig.declarations)
    }

    PermissionRequestScreen(
        gateState = gateState,
        allowSkipOptional = permissionConfig.allowSkipOptional,
        allowSkipRequired = permissionConfig.allowSkipRequired,
        onRequestRuntimePermissionsClick = {
            val runtimePermissions = gateState.permissions
                .filter { !it.granted }
                .mapNotNull { it.declaration.permission.runtimePermissionName() }
                .distinct()
            if (runtimePermissions.isNotEmpty()) {
                runtimePermissionLauncher.launch(runtimePermissions.toTypedArray())
            }
        },
        onOpenSettingsClick = { permission ->
            val intent = permissionIntentFactory.createIntent(permission) ?: return@PermissionRequestScreen
            try {
                context.startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                // Some vendor images omit a matching settings page; keep the user on this screen.
            }
        },
        onRefreshClick = {
            scope.launch {
                val refreshed = permissionRepository.refresh(permissionConfig.declarations)
                if (refreshed.missingRequired.isEmpty() && refreshed.missingOptional.isEmpty()) {
                    onComplete()
                }
            }
        },
        onSkipOptionalClick = {
            scope.launch {
                viewModel.skipOptionalPermissions(
                    gateState.missingOptional.map { it.declaration.permission },
                )
                onComplete()
            }
        },
        onCompleteClick = onComplete,
    )
}
