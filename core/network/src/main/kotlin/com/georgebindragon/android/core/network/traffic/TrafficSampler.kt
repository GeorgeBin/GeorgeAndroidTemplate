package com.georgebindragon.android.core.network.traffic

import kotlinx.coroutines.flow.StateFlow
import java.io.Closeable

interface TrafficSampler : Closeable {
    val trafficTotal: StateFlow<NetworkTrafficTotal>

    val bandwidth: StateFlow<NetworkBandwidth>

    fun startSampling()

    fun stopSampling()
}
