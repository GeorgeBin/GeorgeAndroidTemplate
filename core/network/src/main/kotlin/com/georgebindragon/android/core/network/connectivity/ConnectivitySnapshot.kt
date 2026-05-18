package com.georgebindragon.android.core.network.connectivity

internal data class ConnectivitySnapshot(
    val transports: Set<ConnectivityTransport>,
    val hasInternet: Boolean,
    val isValidated: Boolean,
)

internal enum class ConnectivityTransport {
    Wifi,
    Cellular,
    Ethernet,
    Vpn,
    Bluetooth,
    Other,
}
