package com.georgebindragon.android.core.system

import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.base.shell.ShellExecutor
import com.georgebindragon.android.base.shell.ShellResult

interface RootShellExecutor {
    suspend fun executeAsRoot(
        command: List<String>,
        timeoutMillis: Long? = null,
    ): AppResult<ShellResult>
}

class CapabilityCheckingRootShellExecutor(
    private val capabilityManager: SystemCapabilityManager = DefaultSystemCapabilityManager(),
    private val shellExecutor: ShellExecutor? = null,
) : RootShellExecutor {
    override suspend fun executeAsRoot(
        command: List<String>,
        timeoutMillis: Long?,
    ): AppResult<ShellResult> {
        if (!capabilityManager.isAvailable(SystemCapability.RootShell)) {
            return AppResult.Failure(SystemCapability.RootShell.unavailableError())
        }

        val executor = shellExecutor
            ?: return AppResult.Failure(SystemCapability.RootShell.missingImplementationError())

        return AppResult.Success(executor.execute(listOf("su", "-c", command.joinToString(" ")), timeoutMillis))
    }
}
