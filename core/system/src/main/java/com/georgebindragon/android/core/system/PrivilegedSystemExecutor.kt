package com.georgebindragon.android.core.system

import com.georgebindragon.android.base.common.AppResult

interface PrivilegedSystemExecutor {
    suspend fun <T> executeIfAvailable(
        capability: SystemCapability,
        operation: suspend () -> AppResult<T>,
    ): AppResult<T>
}

class CapabilityCheckingPrivilegedSystemExecutor(
    private val capabilityManager: SystemCapabilityManager = DefaultSystemCapabilityManager(),
) : PrivilegedSystemExecutor {
    override suspend fun <T> executeIfAvailable(
        capability: SystemCapability,
        operation: suspend () -> AppResult<T>,
    ): AppResult<T> {
        if (!capabilityManager.isAvailable(capability)) {
            return AppResult.Failure(capability.unavailableError())
        }
        return operation()
    }
}
