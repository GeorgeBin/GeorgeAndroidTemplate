package com.georgebindragon.android.core.permission

import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.base.common.safeCall
import com.georgebindragon.android.core.datastore.KeyValueStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataStorePermissionRepository(
    private val keyValueStore: KeyValueStore,
    private val permissionChecker: PermissionChecker,
) : PermissionRepository {
    private val mutableGateState = MutableStateFlow(PermissionGateState())
    override val gateState: StateFlow<PermissionGateState> = mutableGateState.asStateFlow()

    override suspend fun refresh(
        declarations: List<AppPermissionDeclaration>,
    ): PermissionGateState {
        val skippedPermissionKeys = keyValueStore.getStringSet(KEY_SKIPPED_PERMISSIONS)
        val state = PermissionGateState(
            permissions = declarations.map { declaration ->
                PermissionState(
                    declaration = declaration,
                    granted = permissionChecker.isGranted(declaration.permission),
                    skipped = declaration.permission.stableKey() in skippedPermissionKeys,
                )
            },
        )
        mutableGateState.value = state
        return state
    }

    override suspend fun shouldShowPermissionGate(
        enabled: Boolean,
        declarations: List<AppPermissionDeclaration>,
        showOverviewOnFirstLaunch: Boolean,
        overviewVersion: Int,
    ): Boolean {
        if (!enabled) return false
        val state = refresh(declarations)
        val seenOverviewVersion = keyValueStore.getInt(KEY_OVERVIEW_VERSION, 0)
        val shouldShowOverview = showOverviewOnFirstLaunch &&
            seenOverviewVersion < overviewVersion
        return shouldShowOverview || state.hasMissingPermissions
    }

    override suspend fun markOverviewSeen(overviewVersion: Int): AppResult<Unit> {
        return safeCall(errorCode = ERROR_MARK_OVERVIEW_SEEN) {
            keyValueStore.putInt(KEY_OVERVIEW_VERSION, overviewVersion)
        }
    }

    override suspend fun skipOptionalPermissions(
        permissions: List<AppPermission>,
    ): AppResult<Unit> {
        return safeCall(errorCode = ERROR_SKIP_OPTIONAL_PERMISSIONS) {
            val current = keyValueStore.getStringSet(KEY_SKIPPED_PERMISSIONS)
            val next = current + permissions.map { it.stableKey() }
            keyValueStore.putStringSet(KEY_SKIPPED_PERMISSIONS, next)
        }
    }

    private companion object {
        const val KEY_OVERVIEW_VERSION = "permission_overview_version"
        const val KEY_SKIPPED_PERMISSIONS = "permission_skipped_permissions"
        const val ERROR_MARK_OVERVIEW_SEEN = "permission_mark_overview_seen_failed"
        const val ERROR_SKIP_OPTIONAL_PERMISSIONS = "permission_skip_optional_failed"
    }
}
