package com.georgebindragon.android.core.startup

import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.core.appconfig.AppConfig
import com.georgebindragon.android.core.appconfig.AppConfigProvider
import com.georgebindragon.android.core.appconfig.DefaultAppConfigProvider
import com.georgebindragon.android.core.appconfig.PermissionFeatureConfig
import com.georgebindragon.android.core.appconfig.PrivacyFeatureConfig
import com.georgebindragon.android.core.permission.AppPermission
import com.georgebindragon.android.core.permission.AppPermissionDeclaration
import com.georgebindragon.android.core.permission.PermissionGateState
import com.georgebindragon.android.core.permission.PermissionRepository
import com.georgebindragon.android.core.privacy.PrivacyAcceptSource
import com.georgebindragon.android.core.privacy.PrivacyRepository
import com.georgebindragon.android.core.privacy.PrivacyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultStartupCoordinatorTest {
    @Test
    fun resolveDestinationDefaultsToMain() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = DefaultAppConfigProvider(),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = false),
            permissionRepository = FakePermissionRepository(shouldShowPermissionGate = false),
        )

        assertEquals(StartupDestination.Main, coordinator.resolveDestination())
    }

    @Test
    fun resolveDestinationReturnsPrivacyWhenPrivacyShouldBeShown() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = DefaultAppConfigProvider(),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = true),
            permissionRepository = FakePermissionRepository(shouldShowPermissionGate = true),
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
            permissionRepository = FakePermissionRepository(shouldShowPermissionGate = false),
        )

        assertEquals(StartupDestination.Main, coordinator.resolveDestination())
    }

    @Test
    fun resolveDestinationReturnsPermissionOverviewWhenPermissionGateShouldBeShown() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = DefaultAppConfigProvider(),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = false),
            permissionRepository = FakePermissionRepository(shouldShowPermissionGate = true),
        )

        assertEquals(StartupDestination.PermissionOverview, coordinator.resolveDestination())
    }

    @Test
    fun resolveDestinationSkipsPermissionWhenPermissionFeatureIsDisabled() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = FixedAppConfigProvider(
                AppConfig(
                    privacy = PrivacyFeatureConfig(enabled = false),
                    permission = PermissionFeatureConfig(enabled = false),
                ),
            ),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = false),
            permissionRepository = FakePermissionRepository(shouldShowPermissionGate = true),
        )

        assertEquals(StartupDestination.Main, coordinator.resolveDestination())
    }

    @Test
    fun resolveDestinationCanUseInjectedInitialDestinationAfterPrivacyGate() = runTest {
        val coordinator = DefaultStartupCoordinator(
            appConfigProvider = DefaultAppConfigProvider(),
            privacyRepository = FakePrivacyRepository(shouldShowPrivacy = false),
            permissionRepository = FakePermissionRepository(shouldShowPermissionGate = false),
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
    override val privacyState = MutableStateFlow(PrivacyState())

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

private class FakePermissionRepository(
    private val shouldShowPermissionGate: Boolean,
) : PermissionRepository {
    override val gateState = MutableStateFlow(PermissionGateState())

    override suspend fun refresh(
        declarations: List<AppPermissionDeclaration>,
    ): PermissionGateState = gateState.value

    override suspend fun shouldShowPermissionGate(
        enabled: Boolean,
        declarations: List<AppPermissionDeclaration>,
        showOverviewOnFirstLaunch: Boolean,
        overviewVersion: Int,
    ): Boolean = enabled && shouldShowPermissionGate

    override suspend fun markOverviewSeen(overviewVersion: Int): AppResult<Unit> {
        return AppResult.Success(Unit)
    }

    override suspend fun skipOptionalPermissions(
        permissions: List<AppPermission>,
    ): AppResult<Unit> = AppResult.Success(Unit)
}
