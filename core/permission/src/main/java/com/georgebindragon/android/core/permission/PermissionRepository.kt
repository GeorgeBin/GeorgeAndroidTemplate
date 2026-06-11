package com.georgebindragon.android.core.permission

import com.georgebindragon.android.base.common.AppResult
import kotlinx.coroutines.flow.StateFlow

interface PermissionRepository {
    val gateState: StateFlow<PermissionGateState>

    suspend fun refresh(declarations: List<AppPermissionDeclaration>): PermissionGateState

    suspend fun shouldShowPermissionGate(
        enabled: Boolean,
        declarations: List<AppPermissionDeclaration>,
        showOverviewOnFirstLaunch: Boolean,
        overviewVersion: Int,
    ): Boolean

    suspend fun markOverviewSeen(overviewVersion: Int): AppResult<Unit>

    suspend fun skipOptionalPermissions(permissions: List<AppPermission>): AppResult<Unit>
}
