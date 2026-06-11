package com.georgebindragon.android.base.networktool

data class PingResult(
    val host: String,
    val transmittedCount: Int,
    val receivedCount: Int,
    val minLatencyMillis: Long? = null,
    val averageLatencyMillis: Long? = null,
    val maxLatencyMillis: Long? = null,
    val errorMessage: String? = null,
) {
    val packetLossPercent: Float
        get() = if (transmittedCount == 0) {
            100f
        } else {
            ((transmittedCount - receivedCount).coerceAtLeast(0) * 100f) / transmittedCount
        }

    val isReachable: Boolean = receivedCount > 0 && errorMessage == null
}
