package com.georgebindragon.android.core.auth

import com.georgebindragon.android.base.common.AppResult
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    fun observeAuthState(): StateFlow<AuthState>
    suspend fun isLoggedIn(): Boolean
    suspend fun login(account: String, password: String): AppResult<Unit>
    suspend fun continueAsGuest(): AppResult<Unit>
    suspend fun logout(): AppResult<Unit>
}
