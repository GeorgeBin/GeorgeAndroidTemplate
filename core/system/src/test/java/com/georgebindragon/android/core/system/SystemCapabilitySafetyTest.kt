package com.georgebindragon.android.core.system

import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.base.shell.ShellExecutor
import com.georgebindragon.android.base.shell.ShellResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SystemCapabilitySafetyTest {
    @Test
    fun defaultCapabilitiesAreUnavailableForNormalApp() {
        val manager = DefaultSystemCapabilityManager()

        SystemCapability.entries.forEach { capability ->
            assertFalse(manager.isAvailable(capability))
        }
    }

    @Test
    fun privilegedExecutorDoesNotRunOperationWhenCapabilityIsUnavailable() = runTest {
        var executed = false
        val executor = CapabilityCheckingPrivilegedSystemExecutor(DefaultSystemCapabilityManager())

        val result = executor.executeIfAvailable(SystemCapability.SilentInstall) {
            executed = true
            AppResult.Success(Unit)
        }

        assertTrue(result is AppResult.Failure)
        assertFalse(executed)
    }

    @Test
    fun rootShellExecutorChecksCapabilityBeforeDelegating() = runTest {
        var delegated = false
        val shellExecutor = object : ShellExecutor {
            override suspend fun execute(
                command: List<String>,
                timeoutMillis: Long?,
                workingDirectory: String?,
            ): ShellResult {
                delegated = true
                return ShellResult(
                    command = command,
                    exitCode = 0,
                    standardOutput = "",
                    standardError = "",
                    durationMillis = 0,
                )
            }
        }
        val rootShellExecutor = CapabilityCheckingRootShellExecutor(
            capabilityManager = DefaultSystemCapabilityManager(),
            shellExecutor = shellExecutor,
        )

        val result = rootShellExecutor.executeAsRoot(listOf("id"))

        assertTrue(result is AppResult.Failure)
        assertFalse(delegated)
    }
}
