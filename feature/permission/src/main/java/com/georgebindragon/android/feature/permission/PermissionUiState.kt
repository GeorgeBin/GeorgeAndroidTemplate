package com.georgebindragon.android.feature.permission

import com.georgebindragon.android.core.permission.PermissionGateState

data class PermissionUiState(
    val gateState: PermissionGateState = PermissionGateState(),
    val loading: Boolean = false,
)
