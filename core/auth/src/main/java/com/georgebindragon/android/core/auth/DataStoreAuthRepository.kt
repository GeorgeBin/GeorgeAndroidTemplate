package com.georgebindragon.android.core.auth

import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.base.common.safeCall
import com.georgebindragon.android.core.datastore.KeyValueStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DataStoreAuthRepository(
    private val keyValueStore: KeyValueStore,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
    private val currentTimeMillis: () -> Long = { System.currentTimeMillis() },
) : AuthRepository {
    private val authState = combine(
        keyValueStore.observeString(KEY_ACCOUNT, ""),
        keyValueStore.observeString(KEY_TOKEN, ""),
        keyValueStore.observeLong(KEY_LOGGED_IN_AT, 0L),
        keyValueStore.observeBoolean(KEY_GUEST, false),
    ) { account, token, loggedInAtMillis, guest ->
        AuthState(
            session = SessionState(
                account = account.orEmpty(),
                token = token.orEmpty(),
                loggedInAtMillis = loggedInAtMillis,
                guest = guest,
            ),
        )
    }.stateIn(scope, SharingStarted.Eagerly, AuthState())

    override fun observeAuthState(): StateFlow<AuthState> = authState

    override suspend fun isLoggedIn(): Boolean {
        return keyValueStore.getString(KEY_TOKEN, "").orEmpty().isNotBlank() ||
            keyValueStore.getBoolean(KEY_GUEST, false)
    }

    override suspend fun login(account: String, password: String): AppResult<Unit> {
        return safeCall(errorCode = ERROR_LOGIN_FAILED) {
            require(account.isNotBlank()) { "account is blank" }
            require(password.isNotBlank()) { "password is blank" }
            keyValueStore.putString(KEY_ACCOUNT, account.trim())
            keyValueStore.putString(KEY_TOKEN, "local-session-${currentTimeMillis()}")
            keyValueStore.putLong(KEY_LOGGED_IN_AT, currentTimeMillis())
            keyValueStore.putBoolean(KEY_GUEST, false)
        }
    }

    override suspend fun continueAsGuest(): AppResult<Unit> {
        return safeCall(errorCode = ERROR_GUEST_FAILED) {
            keyValueStore.putString(KEY_ACCOUNT, "guest")
            keyValueStore.putString(KEY_TOKEN, "")
            keyValueStore.putLong(KEY_LOGGED_IN_AT, currentTimeMillis())
            keyValueStore.putBoolean(KEY_GUEST, true)
        }
    }

    override suspend fun logout(): AppResult<Unit> {
        return safeCall(errorCode = ERROR_LOGOUT_FAILED) {
            keyValueStore.remove(KEY_ACCOUNT)
            keyValueStore.remove(KEY_TOKEN)
            keyValueStore.remove(KEY_LOGGED_IN_AT)
            keyValueStore.remove(KEY_GUEST)
        }
    }

    private companion object {
        const val KEY_ACCOUNT = "auth_account"
        const val KEY_TOKEN = "auth_token"
        const val KEY_LOGGED_IN_AT = "auth_logged_in_at"
        const val KEY_GUEST = "auth_guest"
        const val ERROR_LOGIN_FAILED = "auth_login_failed"
        const val ERROR_GUEST_FAILED = "auth_guest_failed"
        const val ERROR_LOGOUT_FAILED = "auth_logout_failed"
    }
}
