package com.georgebindragon.android.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.georgebindragon.android.core.appconfig.AppIcon
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainShellScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersConfiguredThreeTabs() {
        val clickedRoutes = mutableListOf<String>()

        composeRule.setContent {
            MaterialTheme {
                MainShellScreen(
                    tabs = listOf(
                        MainTabState(
                            route = "home",
                            title = "首页",
                            icon = AppIcon.Home,
                            selected = true,
                        ),
                        MainTabState(
                            route = "message",
                            title = "消息",
                            icon = AppIcon.Message,
                            selected = false,
                        ),
                        MainTabState(
                            route = "settings",
                            title = "设置",
                            icon = AppIcon.Settings,
                            selected = false,
                        ),
                    ),
                    onTabClick = clickedRoutes::add,
                ) {
                    Box {}
                }
            }
        }

        composeRule.onNodeWithText("首页").assertExists()
        composeRule.onNodeWithText("消息").assertExists()
        composeRule.onNodeWithText("设置").assertExists()
        composeRule.onNodeWithContentDescription("消息").assertExists()

        composeRule.onNodeWithContentDescription("消息").performClick()

        assertEquals(listOf("message"), clickedRoutes)
    }
}
