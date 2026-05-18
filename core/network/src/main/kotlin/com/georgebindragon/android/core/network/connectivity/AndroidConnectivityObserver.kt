package com.georgebindragon.android.core.network.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.SystemClock
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AndroidConnectivityObserver(
    context: Context,
) : ConnectivityObserver {
    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val callback = ConnectivityCallback()
    private val scope = CoroutineScope(
        SupervisorJob() +
            Dispatchers.IO +
            CoroutineExceptionHandler { _, _ -> },
    )
    private val mutableState = MutableStateFlow(fetchCurrentState())
    private var isClosed = false

    override val state: StateFlow<NetworkConnectivityState> = mutableState.asStateFlow()

    init {
        registerCallback()
    }

    override fun current(forceRefresh: Boolean): NetworkConnectivityState {
        if (!forceRefresh) {
            return state.value
        }
        return refreshState()
    }

    override fun close() {
        if (isClosed) {
            return
        }
        isClosed = true
        runCatching { connectivityManager.unregisterNetworkCallback(callback) }
        scope.cancel()
    }

    private fun registerCallback() {
        runCatching {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(request, callback)
        }
    }

    private fun refreshState(): NetworkConnectivityState =
        fetchCurrentState().also { mutableState.value = it }

    private fun fetchCurrentState(): NetworkConnectivityState {
        val network = connectivityManager.activeNetwork
            ?: return NetworkConnectivityState.none(SystemClock.elapsedRealtime())
        val capabilities = connectivityManager.getNetworkCapabilities(network)
            ?: return NetworkConnectivityState.none(SystemClock.elapsedRealtime())
        return stateFor(
            network = network,
            capabilities = capabilities,
            linkProperties = connectivityManager.getLinkProperties(network),
        )
    }

    private fun stateFor(
        network: Network,
        capabilities: NetworkCapabilities,
        linkProperties: LinkProperties?,
    ): NetworkConnectivityState = ConnectivityStateMapper.fromSnapshot(
        snapshot = capabilities.toSnapshot(),
        ipAddresses = linkProperties.exposedIpAddresses(),
        networkId = network.toString(),
        updatedAtMillis = SystemClock.elapsedRealtime(),
    )

    private fun NetworkCapabilities.toSnapshot(): ConnectivitySnapshot = ConnectivitySnapshot(
        transports = buildSet {
            if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                add(ConnectivityTransport.Wifi)
            }
            if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                add(ConnectivityTransport.Cellular)
            }
            if (hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                add(ConnectivityTransport.Ethernet)
            }
            if (hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                add(ConnectivityTransport.Vpn)
            }
            if (hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                add(ConnectivityTransport.Bluetooth)
            }
            if (isEmpty()) {
                add(ConnectivityTransport.Other)
            }
        },
        hasInternet = hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET),
        isValidated = hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED),
    )

    private fun LinkProperties?.exposedIpAddresses(): List<String> = this
        ?.linkAddresses
        .orEmpty()
        .map { it.address }
        .filter(IpAddressFilter::shouldExpose)
        .map { it.hostAddress.orEmpty() }
        .filter { it.isNotBlank() }
        .distinct()

    private inner class ConnectivityCallback : ConnectivityManager.NetworkCallback() {
        private var updateJob: Job? = null

        override fun onAvailable(network: Network) {
            scheduleRefresh()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities,
        ) {
            updateJob?.cancel()
            updateJob = scope.launch {
                mutableState.value = stateFor(
                    network = network,
                    capabilities = networkCapabilities,
                    linkProperties = connectivityManager.getLinkProperties(network),
                )
            }
        }

        override fun onLinkPropertiesChanged(
            network: Network,
            linkProperties: LinkProperties,
        ) {
            updateJob?.cancel()
            updateJob = scope.launch {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                mutableState.value = if (capabilities == null) {
                    fetchCurrentState()
                } else {
                    stateFor(
                        network = network,
                        capabilities = capabilities,
                        linkProperties = linkProperties,
                    )
                }
            }
        }

        override fun onLost(network: Network) {
            scheduleRefresh(delayMillis = LOST_REFRESH_DELAY_MILLIS)
        }

        private fun scheduleRefresh(delayMillis: Long = 0L) {
            updateJob?.cancel()
            updateJob = scope.launch {
                if (delayMillis > 0L) {
                    delay(delayMillis)
                }
                refreshState()
            }
        }
    }

    private companion object {
        const val LOST_REFRESH_DELAY_MILLIS = 1_000L
    }
}
