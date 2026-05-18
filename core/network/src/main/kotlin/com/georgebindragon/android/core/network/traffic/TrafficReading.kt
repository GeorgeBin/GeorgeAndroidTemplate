package com.georgebindragon.android.core.network.traffic

internal data class TrafficReading(
    val rxBytes: Long,
    val txBytes: Long,
    val timestampMillis: Long,
) {
    val isSupported: Boolean = rxBytes >= 0L && txBytes >= 0L
}
