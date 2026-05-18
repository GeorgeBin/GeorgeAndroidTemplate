package com.georgebindragon.android.core.network.connectivity

data class NetworkConnectivityState(
    val type: NetworkType,
    val isConnected: Boolean,
    val isValidated: Boolean,
    val ipAddresses: List<String>,
    val networkId: String,
    val updatedAtMillis: Long,
) {
    companion object {
        fun none(updatedAtMillis: Long): NetworkConnectivityState = NetworkConnectivityState(
            type = NetworkType.None,
            isConnected = false,
            isValidated = false,
            ipAddresses = emptyList(),
            networkId = "",
            updatedAtMillis = updatedAtMillis,
        )
    }
}
