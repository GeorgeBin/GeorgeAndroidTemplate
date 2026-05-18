package com.georgebindragon.android.core.network.connectivity

internal object ConnectivityStateMapper {
    fun fromSnapshot(
        snapshot: ConnectivitySnapshot,
        ipAddresses: List<String>,
        networkId: String,
        updatedAtMillis: Long,
    ): NetworkConnectivityState {
        if (!snapshot.hasInternet) {
            return NetworkConnectivityState.none(updatedAtMillis)
        }

        val type = resolveType(snapshot.transports)
        return NetworkConnectivityState(
            type = type,
            isConnected = type != NetworkType.None,
            isValidated = snapshot.isValidated,
            ipAddresses = ipAddresses,
            networkId = networkId,
            updatedAtMillis = updatedAtMillis,
        )
    }

    private fun resolveType(transports: Set<ConnectivityTransport>): NetworkType = when {
        ConnectivityTransport.Vpn in transports -> NetworkType.Vpn
        ConnectivityTransport.Wifi in transports -> NetworkType.Wifi
        ConnectivityTransport.Cellular in transports -> NetworkType.Cellular
        ConnectivityTransport.Ethernet in transports -> NetworkType.Ethernet
        ConnectivityTransport.Bluetooth in transports -> NetworkType.Bluetooth
        transports.isNotEmpty() -> NetworkType.Unknown
        else -> NetworkType.Unknown
    }
}
