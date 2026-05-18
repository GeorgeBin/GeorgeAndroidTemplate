package com.georgebindragon.android.core.network.connectivity

import kotlinx.coroutines.flow.StateFlow
import java.io.Closeable

interface ConnectivityObserver : Closeable {
    val state: StateFlow<NetworkConnectivityState>

    fun current(forceRefresh: Boolean = false): NetworkConnectivityState
}
