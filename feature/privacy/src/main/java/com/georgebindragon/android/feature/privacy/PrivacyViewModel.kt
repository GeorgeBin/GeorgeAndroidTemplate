package com.georgebindragon.android.feature.privacy

import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.core.appconfig.PrivacyFeatureConfig
import com.georgebindragon.android.core.privacy.PrivacyAcceptSource
import com.georgebindragon.android.core.privacy.PrivacyRepository

class PrivacyViewModel(
    private val privacyRepository: PrivacyRepository,
    privacyConfig: PrivacyFeatureConfig,
) {
    val initialState = PrivacyUiState(
        privacyVersion = privacyConfig.privacyVersion,
        userAgreementVersion = privacyConfig.userAgreementVersion,
    )

    suspend fun acceptPrivacy(): AppResult<Unit> {
        return privacyRepository.acceptPrivacy(
            privacyVersion = initialState.privacyVersion,
            userAgreementVersion = initialState.userAgreementVersion,
            source = PrivacyAcceptSource.Startup,
        )
    }
}
