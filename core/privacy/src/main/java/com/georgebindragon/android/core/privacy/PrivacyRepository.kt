package com.georgebindragon.android.core.privacy

import com.georgebindragon.android.base.common.AppResult
import kotlinx.coroutines.flow.StateFlow

interface PrivacyRepository {
    val privacyState: StateFlow<PrivacyState>

    suspend fun shouldShowPrivacy(
        privacyVersion: Int,
        userAgreementVersion: Int,
    ): Boolean

    suspend fun acceptPrivacy(
        privacyVersion: Int,
        userAgreementVersion: Int,
        source: PrivacyAcceptSource = PrivacyAcceptSource.Startup,
    ): AppResult<Unit>
}
