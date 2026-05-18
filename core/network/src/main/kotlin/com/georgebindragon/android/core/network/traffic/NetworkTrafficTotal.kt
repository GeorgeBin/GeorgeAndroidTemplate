package com.georgebindragon.android.core.network.traffic

data class NetworkTrafficTotal(
    val sampleId: Int,
    val downloadBytes: Long,
    val uploadBytes: Long,
    val timestampMillis: Long,
) {
    companion object {
        val ZERO = NetworkTrafficTotal(
            sampleId = 0,
            downloadBytes = 0L,
            uploadBytes = 0L,
            timestampMillis = 0L,
        )
    }
}
