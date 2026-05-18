package com.georgebindragon.android.core.network.connectivity

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.InetAddress

class IpAddressFilterTest {
    @Test
    fun `exposes private and global unicast addresses`() {
        assertTrue(IpAddressFilter.shouldExpose(InetAddress.getByName("192.168.1.10")))
        assertTrue(IpAddressFilter.shouldExpose(InetAddress.getByName("10.0.0.5")))
        assertTrue(IpAddressFilter.shouldExpose(InetAddress.getByName("2001:db8::1")))
    }

    @Test
    fun `filters non-routable local and multicast addresses`() {
        assertFalse(IpAddressFilter.shouldExpose(InetAddress.getByName("0.0.0.0")))
        assertFalse(IpAddressFilter.shouldExpose(InetAddress.getByName("127.0.0.1")))
        assertFalse(IpAddressFilter.shouldExpose(InetAddress.getByName("169.254.1.1")))
        assertFalse(IpAddressFilter.shouldExpose(InetAddress.getByName("224.0.0.1")))
        assertFalse(IpAddressFilter.shouldExpose(InetAddress.getByName("::1")))
        assertFalse(IpAddressFilter.shouldExpose(InetAddress.getByName("fe80::1")))
    }
}
