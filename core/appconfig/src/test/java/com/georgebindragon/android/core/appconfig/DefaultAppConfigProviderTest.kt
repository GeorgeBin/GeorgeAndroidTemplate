package com.georgebindragon.android.core.appconfig

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultAppConfigProviderTest {
    @Test
    fun defaultConfigContainsVisibleHomeAndSettingsTabs() {
        val tabs = DefaultAppConfigProvider().getConfig().main.tabs

        assertEquals(listOf("home", "settings"), tabs.filter { it.visible }.map { it.route })
        assertTrue(tabs.all { it.visible })
    }
}
