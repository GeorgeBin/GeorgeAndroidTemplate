package com.georgebindragon.android.core.system

interface SystemCapabilityManager {
    fun currentCapabilities(): SystemCapabilities

    fun isAvailable(capability: SystemCapability): Boolean =
        currentCapabilities().isAvailable(capability)
}

class DefaultSystemCapabilityManager(
    private val capabilities: SystemCapabilities = SystemCapabilities.unavailable(),
) : SystemCapabilityManager {
    override fun currentCapabilities(): SystemCapabilities = capabilities
}
