package com.georgebindragon.android.core.system

import com.georgebindragon.android.base.common.AppResult

interface SilentInstallManager {
    suspend fun install(apkPath: String): AppResult<Unit>
    suspend fun uninstall(packageName: String): AppResult<Unit>
}

class DefaultSilentInstallManager(
    private val privilegedSystemExecutor: PrivilegedSystemExecutor = CapabilityCheckingPrivilegedSystemExecutor(),
) : SilentInstallManager {
    override suspend fun install(apkPath: String): AppResult<Unit> =
        privilegedSystemExecutor.executeIfAvailable(SystemCapability.SilentInstall) {
            AppResult.Failure(SystemCapability.SilentInstall.missingImplementationError())
        }

    override suspend fun uninstall(packageName: String): AppResult<Unit> =
        privilegedSystemExecutor.executeIfAvailable(SystemCapability.SilentInstall) {
            AppResult.Failure(SystemCapability.SilentInstall.missingImplementationError())
        }
}
