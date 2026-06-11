package com.georgebindragon.android.feature.auth

import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.core.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val authRepository: AuthRepository,
) {
    private val mutableUiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = mutableUiState.asStateFlow()

    fun onAccountChange(account: String) {
        mutableUiState.value = mutableUiState.value.copy(
            account = account,
            errorMessage = null,
        )
    }

    fun onPasswordChange(password: String) {
        mutableUiState.value = mutableUiState.value.copy(
            password = password,
            errorMessage = null,
        )
    }

    suspend fun login(): Boolean {
        val state = mutableUiState.value
        if (state.account.isBlank() || state.password.isBlank()) {
            mutableUiState.value = state.copy(errorMessage = "请输入账号和密码")
            return false
        }
        mutableUiState.value = state.copy(loading = true, errorMessage = null)
        val result = authRepository.login(
            account = state.account,
            password = state.password,
        )
        mutableUiState.value = mutableUiState.value.copy(loading = false)
        return result.toLoginResult()
    }

    suspend fun continueAsGuest(): Boolean {
        mutableUiState.value = mutableUiState.value.copy(loading = true, errorMessage = null)
        val result = authRepository.continueAsGuest()
        mutableUiState.value = mutableUiState.value.copy(loading = false)
        return result.toLoginResult()
    }

    private fun AppResult<Unit>.toLoginResult(): Boolean {
        return when (this) {
            is AppResult.Success -> true
            is AppResult.Failure -> {
                mutableUiState.value = mutableUiState.value.copy(
                    errorMessage = error.message ?: "登录失败",
                )
                false
            }
        }
    }
}
