package com.georgebindragon.android.core.privacy

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

class DataStorePrivacyRepository(
    private val keyValueStore: KeyValueStore,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
    private val currentTimeMillis: () -> Long = { System.currentTimeMillis() },
) : PrivacyRepository {
    override val privacyState: StateFlow<PrivacyState> = combine(
        keyValueStore.observeInt(KEY_PRIVACY_VERSION, 0),
        keyValueStore.observeInt(KEY_USER_AGREEMENT_VERSION, 0),
        keyValueStore.observeLong(KEY_ACCEPTED_AT_MILLIS, 0L),
        keyValueStore.observeString(KEY_ACCEPTED_SOURCE, PrivacyAcceptSource.None.name),
    ) { privacyVersion, userAgreementVersion, acceptedAtMillis, source ->
        PrivacyState(
            acceptedPrivacyVersion = privacyVersion,
            acceptedUserAgreementVersion = userAgreementVersion,
            acceptedAtMillis = acceptedAtMillis,
            acceptedSource = source.toPrivacyAcceptSourceOrDefault(),
        )
    }.stateIn(scope, SharingStarted.Eagerly, PrivacyState())

    override suspend fun shouldShowPrivacy(
        privacyVersion: Int,
        userAgreementVersion: Int,
    ): Boolean {
        val acceptedPrivacyVersion = keyValueStore.getInt(KEY_PRIVACY_VERSION, 0)
        val acceptedUserAgreementVersion = keyValueStore.getInt(KEY_USER_AGREEMENT_VERSION, 0)
        return acceptedPrivacyVersion < privacyVersion ||
            acceptedUserAgreementVersion < userAgreementVersion
    }

    override suspend fun acceptPrivacy(
        privacyVersion: Int,
        userAgreementVersion: Int,
        source: PrivacyAcceptSource,
    ): AppResult<Unit> {
        return safeCall(errorCode = ERROR_ACCEPT_PRIVACY) {
            keyValueStore.putInt(KEY_PRIVACY_VERSION, privacyVersion)
            keyValueStore.putInt(KEY_USER_AGREEMENT_VERSION, userAgreementVersion)
            keyValueStore.putLong(KEY_ACCEPTED_AT_MILLIS, currentTimeMillis())
            keyValueStore.putString(KEY_ACCEPTED_SOURCE, source.name)
        }
    }

    private fun String?.toPrivacyAcceptSourceOrDefault(): PrivacyAcceptSource {
        if (this == null) return PrivacyAcceptSource.None
        return runCatching { enumValueOf<PrivacyAcceptSource>(this) }
            .getOrDefault(PrivacyAcceptSource.None)
    }

    private companion object {
        const val KEY_PRIVACY_VERSION = "privacy_accepted_privacy_version"
        const val KEY_USER_AGREEMENT_VERSION = "privacy_accepted_user_agreement_version"
        const val KEY_ACCEPTED_AT_MILLIS = "privacy_accepted_at_millis"
        const val KEY_ACCEPTED_SOURCE = "privacy_accepted_source"
        const val ERROR_ACCEPT_PRIVACY = "privacy_accept_failed"
    }
}
