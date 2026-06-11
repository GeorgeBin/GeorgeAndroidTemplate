package com.georgebindragon.android.core.startup

import com.georgebindragon.android.core.appconfig.AppConfigProvider
import com.georgebindragon.android.core.privacy.PrivacyRepository

class DefaultStartupCoordinator(
    private val appConfigProvider: AppConfigProvider,
    private val privacyRepository: PrivacyRepository,
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
        return initialDestination
    }
}
