package com.georgebindragon.android.core.network.traffic

internal class BandwidthEstimator(
    decay: Float = DEFAULT_DECAY,
    private val lowerBoundKbps: Double = BANDWIDTH_LOWER_BOUND_KBPS,
) {
    private val downloadAverage = ExponentialMovingAverage(decay)
    private val uploadAverage = ExponentialMovingAverage(decay)

    fun record(
        rxBytesDiff: Long,
        txBytesDiff: Long,
        timeDiffMillis: Long,
    ): NetworkBandwidth? {
        recordBandwidth(downloadAverage, rxBytesDiff, timeDiffMillis)
        recordBandwidth(uploadAverage, txBytesDiff, timeDiffMillis)

        val downloadKbps = downloadAverage.average()
        val uploadKbps = uploadAverage.average()
        return if (downloadKbps >= 0.0 || uploadKbps >= 0.0) {
            NetworkBandwidth(
                downloadKbps = downloadKbps.coerceAtLeast(0.0),
                uploadKbps = uploadKbps.coerceAtLeast(0.0),
            )
        } else {
            null
        }
    }

    fun reset() {
        downloadAverage.reset()
        uploadAverage.reset()
    }

    private fun recordBandwidth(
        average: ExponentialMovingAverage,
        bytesDiff: Long,
        timeDiffMillis: Long,
    ) {
        if (bytesDiff < 0L || timeDiffMillis <= 0L) {
            return
        }

        val kbps = bytesDiff * BITS_PER_BYTE.toDouble() / timeDiffMillis
        if (kbps >= lowerBoundKbps) {
            average.add(kbps)
        }
    }

    private companion object {
        const val DEFAULT_DECAY = 0.2f
        const val BANDWIDTH_LOWER_BOUND_KBPS = 10.0
        const val BITS_PER_BYTE = 8
    }
}
