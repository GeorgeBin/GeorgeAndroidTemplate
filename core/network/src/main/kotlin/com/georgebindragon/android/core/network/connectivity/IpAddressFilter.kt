package com.georgebindragon.android.core.network.connectivity

import java.net.InetAddress

internal object IpAddressFilter {
    fun shouldExpose(address: InetAddress): Boolean =
        !address.isAnyLocalAddress &&
            !address.isLoopbackAddress &&
            !address.isLinkLocalAddress &&
            !address.isMulticastAddress
}
