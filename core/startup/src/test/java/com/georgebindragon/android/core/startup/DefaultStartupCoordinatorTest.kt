package com.georgebindragon.android.core.startup

import com.georgebindragon.android.core.appconfig.DefaultAppConfigProvider
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultStartupCoordinatorTest {
    @Test
    fun resolveDestinationDefaultsToMain() = runTest {
        val coordinator = DefaultStartupCoordinator(DefaultAppConfigProvider())

        assertEquals(StartupDestination.Main, coordinator.resolveDestination())
    }

    @Test
    fun resolveDestinationCanUseInjectedInitialDestinationForSkeletonFlows() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = DefaultAppConfigProvider(),
            initialDestination = StartupDestination.Privacy,
        )

        assertEquals(StartupDestination.Privacy, coordinator.resolveDestination())
    }
}
