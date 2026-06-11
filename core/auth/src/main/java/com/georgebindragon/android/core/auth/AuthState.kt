package com.georgebindragon.android.core.auth

data class SessionState(
    val account: String = "",
    val token: String = "",
    val loggedInAtMillis: Long = 0L,
    val guest: Boolean = false,
) {
    val isActive: Boolean
        get() = token.isNotBlank() || guest
}

data class AuthState(
    val session: SessionState = SessionState(),
) {
    val loggedIn: Boolean
        get() = session.isActive
}
