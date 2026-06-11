package com.georgebindragon.android.feature.permission

import com.georgebindragon.android.core.permission.AppPermission
import com.georgebindragon.android.core.permission.AppPermissionDeclaration
import com.georgebindragon.android.core.permission.PermissionRepository
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel(
    private val permissionRepository: PermissionRepository,
) {
    val gateState: StateFlow<com.georgebindragon.android.core.permission.PermissionGateState> =
        permissionRepository.gateState

    suspend fun refresh(declarations: List<AppPermissionDeclaration>) {
        permissionRepository.refresh(declarations)
    }

    suspend fun markOverviewSeen(overviewVersion: Int) {
        permissionRepository.markOverviewSeen(overviewVersion)
    }

    suspend fun skipOptionalPermissions(permissions: List<AppPermission>) {
        permissionRepository.skipOptionalPermissions(permissions)
    }
}
