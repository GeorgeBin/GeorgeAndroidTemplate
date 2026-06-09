package com.georgebindragon.android.core.startup

import com.georgebindragon.android.core.appconfig.AppConfigProvider

class DefaultStartupCoordinator(
    private val appConfigProvider: AppConfigProvider,
    private val initialDestination: StartupDestination = StartupDestination.Main,
) : StartupCoordinator {
    override suspend fun resolveDestination(): StartupDestination {
        appConfigProvider.getConfig()
        return initialDestination
    }
}
