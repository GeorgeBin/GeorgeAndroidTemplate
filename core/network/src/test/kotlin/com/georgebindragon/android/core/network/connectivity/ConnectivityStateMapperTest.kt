package com.georgebindragon.android.core.network.connectivity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ConnectivityStateMapperTest {
    @Test
    fun `snapshot without internet returns none`() {
        val state = ConnectivityStateMapper.fromSnapshot(
            snapshot = ConnectivitySnapshot(
                transports = setOf(ConnectivityTransport.Wifi),
                hasInternet = false,
                isValidated = true,
            ),
            ipAddresses = listOf("192.168.1.2"),
            networkId = "network-1",
            updatedAtMillis = 100L,
        )

        assertEquals(NetworkType.None, state.type)
        assertFalse(state.isConnected)
        assertFalse(state.isValidated)
        assertEquals(emptyList<String>(), state.ipAddresses)
        assertEquals("", state.networkId)
        assertEquals(100L, state.updatedAtMillis)
    }

    @Test
    fun `wifi with validated internet maps to connected wifi`() {
        val state = ConnectivityStateMapper.fromSnapshot(
            snapshot = ConnectivitySnapshot(
                transports = setOf(ConnectivityTransport.Wifi),
                hasInternet = true,
                isValidated = true,
            ),
            ipAddresses = listOf("192.168.1.2"),
            networkId = "network-1",
            updatedAtMillis = 100L,
        )

        assertEquals(NetworkType.Wifi, state.type)
        assertTrue(state.isConnected)
        assertTrue(state.isValidated)
        assertEquals(listOf("192.168.1.2"), state.ipAddresses)
        assertEquals("network-1", state.networkId)
    }

    @Test
    fun `cellular with unvalidated internet remains connected but not validated`() {
        val state = ConnectivityStateMapper.fromSnapshot(
            snapshot = ConnectivitySnapshot(
                transports = setOf(ConnectivityTransport.Cellular),
                hasInternet = true,
                isValidated = false,
            ),
            ipAddresses = emptyList(),
            networkId = "network-2",
            updatedAtMillis = 100L,
        )

        assertEquals(NetworkType.Cellular, state.type)
        assertTrue(state.isConnected)
        assertFalse(state.isValidated)
    }

    @Test
    fun `vpn wins over underlying transports`() {
        val state = ConnectivityStateMapper.fromSnapshot(
            snapshot = ConnectivitySnapshot(
                transports = setOf(
                    ConnectivityTransport.Vpn,
                    ConnectivityTransport.Wifi,
                    ConnectivityTransport.Cellular,
                ),
                hasInternet = true,
                isValidated = true,
            ),
            ipAddresses = emptyList(),
            networkId = "network-3",
            updatedAtMillis = 100L,
        )

        assertEquals(NetworkType.Vpn, state.type)
    }

    @Test
    fun `transport priority maps common types`() {
        assertType(NetworkType.Ethernet, ConnectivityTransport.Ethernet)
        assertType(NetworkType.Bluetooth, ConnectivityTransport.Bluetooth)
        assertType(NetworkType.Unknown, ConnectivityTransport.Other)
    }

    private fun assertType(
        expected: NetworkType,
        transport: ConnectivityTransport,
    ) {
        val state = ConnectivityStateMapper.fromSnapshot(
            snapshot = ConnectivitySnapshot(
                transports = setOf(transport),
                hasInternet = true,
                isValidated = true,
            ),
            ipAddresses = emptyList(),
            networkId = "network",
            updatedAtMillis = 100L,
        )

        assertEquals(expected, state.type)
        assertTrue(state.isConnected)
    }
}
