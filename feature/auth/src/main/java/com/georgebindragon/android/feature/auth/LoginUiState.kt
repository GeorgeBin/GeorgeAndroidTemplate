package com.georgebindragon.android.feature.auth

data class LoginUiState(
    val account: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val errorMessage: String? = null,
)
