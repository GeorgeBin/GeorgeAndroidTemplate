package com.georgebindragon.android.core.startup

import com.georgebindragon.android.core.appconfig.AppConfigProvider
import com.georgebindragon.android.core.auth.AuthRepository
import com.georgebindragon.android.core.permission.PermissionRepository
import com.georgebindragon.android.core.privacy.PrivacyRepository

class DefaultStartupCoordinator(
    private val appConfigProvider: AppConfigProvider,
    private val privacyRepository: PrivacyRepository,
    private val permissionRepository: PermissionRepository,
    private val authRepository: AuthRepository,
    private val initialDestination: StartupDestination = StartupDestination.Main,
) : StartupCoordinator {
    override suspend fun resolveDestination(): StartupDestination {
        val appConfig = appConfigProvider.getConfig()
        if (appConfig.privacy.enabled && privacyRepository.shouldShowPrivacy(
                privacyVersion = appConfig.privacy.privacyVersion,
                userAgreementVersion = appConfig.privacy.userAgreementVersion,
            )
        ) {
            return StartupDestination.Privacy
        }
        if (permissionRepository.shouldShowPermissionGate(
                enabled = appConfig.permission.enabled,
                declarations = appConfig.permission.declarations,
                showOverviewOnFirstLaunch = appConfig.permission.showOverviewOnFirstLaunch,
                overviewVersion = appConfig.permission.overviewVersion,
            )
        ) {
            return StartupDestination.PermissionOverview
        }
        if (appConfig.auth.enabled && appConfig.auth.required && !authRepository.isLoggedIn()) {
            return StartupDestination.Login
        }
        return initialDestination
    }
}
