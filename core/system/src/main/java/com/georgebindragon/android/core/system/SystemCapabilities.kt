package com.georgebindragon.android.core.system

data class SystemCapabilities(
    private val capabilities: Map<SystemCapability, SystemCapabilityStatus> = emptyMap(),
) {
    fun statusOf(capability: SystemCapability): SystemCapabilityStatus =
        capabilities[capability] ?: SystemCapabilityStatus.Unavailable(DEFAULT_UNAVAILABLE_REASON)

    fun isAvailable(capability: SystemCapability): Boolean = statusOf(capability).available

    companion object {
        private const val DEFAULT_UNAVAILABLE_REASON = "Capability is not available in a normal app process."

        fun unavailable(): SystemCapabilities = SystemCapabilities(
            SystemCapability.entries.associateWith {
                SystemCapabilityStatus.Unavailable(DEFAULT_UNAVAILABLE_REASON)
            },
        )
    }
}

enum class SystemCapability {
    RootShell,
    SilentInstall,
    SystemPermissionManagement,
    PrivilegedExecution,
}

sealed interface SystemCapabilityStatus {
    val available: Boolean
    val reason: String?

    data object Available : SystemCapabilityStatus {
        override val available: Boolean = true
        override val reason: String? = null
    }

    data class Unavailable(
        override val reason: String,
    ) : SystemCapabilityStatus {
        override val available: Boolean = false
    }
}
