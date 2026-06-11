package com.georgebindragon.android.feature.privacy

data class PrivacyUiState(
    val privacyVersion: Int,
    val userAgreementVersion: Int,
    val accepting: Boolean = false,
    val errorMessage: String? = null,
)
