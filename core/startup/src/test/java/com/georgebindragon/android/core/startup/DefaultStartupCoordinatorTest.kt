package com.georgebindragon.android.core.startup

import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.core.appconfig.AppConfig
import com.georgebindragon.android.core.appconfig.AppConfigProvider
import com.georgebindragon.android.core.appconfig.DefaultAppConfigProvider
import com.georgebindragon.android.core.appconfig.PrivacyFeatureConfig
import com.georgebindragon.android.core.privacy.PrivacyAcceptSource
import com.georgebindragon.android.core.privacy.PrivacyRepository
import com.georgebindragon.android.core.privacy.PrivacyState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultStartupCoordinatorTest {
    @Test
    fun resolveDestinationDefaultsToMain() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = DefaultAppConfigProvider(),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = false),
        )

        assertEquals(StartupDestination.Main, coordinator.resolveDestination())
    }

    @Test
    fun resolveDestinationReturnsPrivacyWhenPrivacyShouldBeShown() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = DefaultAppConfigProvider(),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = true),
        )

        assertEquals(StartupDestination.Privacy, coordinator.resolveDestination())
    }

    @Test
    fun resolveDestinationSkipsPrivacyWhenPrivacyFeatureIsDisabled() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = FixedAppConfigProvider(
                AppConfig(
                    privacy = PrivacyFeatureConfig(enabled = false),
                ),
            ),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = true),
        )

        assertEquals(StartupDestination.Main, coordinator.resolveDestination())
    }

    @Test
    fun resolveDestinationCanUseInjectedInitialDestinationAfterPrivacyGate() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = DefaultAppConfigProvider(),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = false),
            initialDestination = StartupDestination.Login,
        )

        assertEquals(StartupDestination.Login, coordinator.resolveDestination())
    }
}

private class FixedAppConfigProvider(
    private val appConfig: AppConfig,
) : AppConfigProvider {
    override fun getConfig(): AppConfig = appConfig
}

private class FakePrivacyRepository(
    private val shouldShowPrivacy: Boolean,
) : PrivacyRepository {
    override val privacyState = kotlinx.coroutines.flow.MutableStateFlow(PrivacyState())

    override suspend fun shouldShowPrivacy(
        privacyVersion: Int,
        userAgreementVersion: Int,
    ): Boolean = shouldShowPrivacy

    override suspend fun acceptPrivacy(
        privacyVersion: Int,
        userAgreementVersion: Int,
        source: PrivacyAcceptSource,
    ): AppResult<Unit> = AppResult.Success(Unit)
}
