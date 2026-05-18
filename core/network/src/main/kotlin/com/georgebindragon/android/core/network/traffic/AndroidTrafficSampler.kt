package com.georgebindragon.android.core.network.traffic

import android.content.Context
import android.net.TrafficStats
import android.os.SystemClock
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class AndroidTrafficSampler(
    context: Context,
    private val sampleIntervalMillis: Long = DEFAULT_SAMPLE_INTERVAL_MILLIS,
) : TrafficSampler {
    private val applicationContext = context.applicationContext
    private val uid = applicationContext.applicationInfo.uid
    private val scope = CoroutineScope(
        SupervisorJob() +
            Dispatchers.IO +
            CoroutineExceptionHandler { _, _ -> },
    )
    private val samplingCounter = SamplingReferenceCounter()
    private val samplingTaskId = AtomicInteger(0)
    private val bandwidthEstimator = BandwidthEstimator()
    private val mutableTrafficTotal = MutableStateFlow(NetworkTrafficTotal.ZERO)
    private val mutableBandwidth = MutableStateFlow(NetworkBandwidth.UNDEFINED)
    private var isClosed = false

    override val trafficTotal: StateFlow<NetworkTrafficTotal> = mutableTrafficTotal.asStateFlow()
    override val bandwidth: StateFlow<NetworkBandwidth> = mutableBandwidth.asStateFlow()

    override fun startSampling() {
        if (isClosed || !samplingCounter.increment()) {
            return
        }

        val sampleId = samplingTaskId.incrementAndGet()
        scope.launch {
            sampleLoop(sampleId)
        }
    }

    override fun stopSampling() {
        if (!samplingCounter.decrement()) {
            return
        }

        stopCurrentSamplingTask()
    }

    override fun close() {
        if (isClosed) {
            return
        }
        isClosed = true
        stopCurrentSamplingTask()
        scope.cancel()
    }

    private suspend fun sampleLoop(sampleId: Int) {
        var baseline: TrafficReading? = null
        var previous: TrafficReading? = null
        while (currentCoroutineContext().isActive && samplingTaskId.get() == sampleId) {
            val current = readTraffic()
            val timestampMillis = SystemClock.elapsedRealtime()
            if (current.isSupported) {
                val start = baseline
                val last = previous
                if (start == null) {
                    baseline = current
                } else {
                    mutableTrafficTotal.value = NetworkTrafficTotal(
                        sampleId = sampleId,
                        downloadBytes = current.rxBytes - start.rxBytes,
                        uploadBytes = current.txBytes - start.txBytes,
                        timestampMillis = timestampMillis,
                    )
                }

                if (last != null) {
                    val bandwidthSample = bandwidthEstimator.record(
                        rxBytesDiff = current.rxBytes - last.rxBytes,
                        txBytesDiff = current.txBytes - last.txBytes,
                        timeDiffMillis = timestampMillis - last.timestampMillis,
                    )
                    if (bandwidthSample != null) {
                        mutableBandwidth.value = bandwidthSample
                    }
                }
                previous = current.copy(timestampMillis = timestampMillis)
            }
            delay(sampleIntervalMillis)
        }
    }

    private fun stopCurrentSamplingTask() {
        val current = samplingTaskId.get()
        samplingTaskId.compareAndSet(current, current + 1)
        samplingCounter.reset()
        bandwidthEstimator.reset()
    }

    private fun readTraffic(): TrafficReading = TrafficReading(
        rxBytes = TrafficStats.getUidRxBytes(uid),
        txBytes = TrafficStats.getUidTxBytes(uid),
        timestampMillis = SystemClock.elapsedRealtime(),
    )

    private companion object {
        const val DEFAULT_SAMPLE_INTERVAL_MILLIS = 2_000L
    }
}
