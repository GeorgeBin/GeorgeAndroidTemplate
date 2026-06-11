package com.georgebindragon.android.core.system

import com.georgebindragon.android.base.common.AppResult

interface SystemPermissionManager {
    suspend fun grantPermission(packageName: String, permission: String): AppResult<Unit>
    suspend fun revokePermission(packageName: String, permission: String): AppResult<Unit>
}

class DefaultSystemPermissionManager(
    private val privilegedSystemExecutor: PrivilegedSystemExecutor = CapabilityCheckingPrivilegedSystemExecutor(),
) : SystemPermissionManager {
    override suspend fun grantPermission(packageName: String, permission: String): AppResult<Unit> =
        privilegedSystemExecutor.executeIfAvailable(SystemCapability.SystemPermissionManagement) {
            AppResult.Failure(SystemCapability.SystemPermissionManagement.missingImplementationError())
        }

    override suspend fun revokePermission(packageName: String, permission: String): AppResult<Unit> =
        privilegedSystemExecutor.executeIfAvailable(SystemCapability.SystemPermissionManagement) {
            AppResult.Failure(SystemCapability.SystemPermissionManagement.missingImplementationError())
        }
}
