package com.georgebindragon.android.core.privacy

data class PrivacyState(
    val acceptedPrivacyVersion: Int = 0,
    val acceptedUserAgreementVersion: Int = 0,
    val acceptedAtMillis: Long = 0L,
    val acceptedSource: PrivacyAcceptSource = PrivacyAcceptSource.None,
)

enum class PrivacyAcceptSource {
    None,
    Startup,
    Settings,
}
