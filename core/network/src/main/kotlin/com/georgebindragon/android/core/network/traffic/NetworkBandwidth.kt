package com.georgebindragon.android.core.network.traffic

data class NetworkBandwidth(
    val downloadKbps: Double,
    val uploadKbps: Double,
) {
    companion object {
        val UNDEFINED = NetworkBandwidth(
            downloadKbps = 0.0,
            uploadKbps = 0.0,
        )
    }
}
