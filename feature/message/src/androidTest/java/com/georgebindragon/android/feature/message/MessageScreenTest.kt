package com.georgebindragon.android.feature.message

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.longClick
import org.junit.Rule
import org.junit.Test

class MessageScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersSearchListHeaderItemsAndFooter() {
        composeRule.setContent {
            MaterialTheme {
                MessageScreen(
                    uiState = MessageUiState(),
                    snackbarHostState = SnackbarHostState(),
                    onQueryChange = {},
                    onMarkRead = {},
                    onTogglePinned = {},
                    onDelete = {},
                    onShowDetail = {},
                )
            }
        }

        composeRule.onNodeWithText("消息").assertExists()
        composeRule.onNodeWithText("搜索消息").assertExists()
        composeRule.onNodeWithText("共 5 条消息，当前显示 5 条").assertExists()
        composeRule.onNodeWithText("版本更新提醒").assertExists()
        composeRule.onNodeWithText("已显示全部消息").assertExists()
    }

    @Test
    fun filtersMessagesBySearchKeyword() {
        var uiState by mutableStateOf(MessageUiState())

        composeRule.setContent {
            MaterialTheme {
                MessageScreen(
                    uiState = uiState,
                    snackbarHostState = SnackbarHostState(),
                    onQueryChange = { uiState = uiState.copy(query = it) },
                    onMarkRead = {},
                    onTogglePinned = {},
                    onDelete = {},
                    onShowDetail = {},
                )
            }
        }

        composeRule.onNodeWithText("搜索消息").performTextInput("权限")

        composeRule.onNodeWithText("权限检查完成").assertExists()
        composeRule.onNodeWithText("共 5 条消息，当前显示 1 条").assertExists()
    }

    @Test
    fun longClickShowsMessageActions() {
        composeRule.setContent {
            MaterialTheme {
                MessageScreen(
                    uiState = MessageUiState(),
                    snackbarHostState = SnackbarHostState(),
                    onQueryChange = {},
                    onMarkRead = {},
                    onTogglePinned = {},
                    onDelete = {},
                    onShowDetail = {},
                )
            }
        }

        composeRule.onNodeWithText("版本更新提醒").performTouchInput {
            longClick()
        }

        composeRule.onNodeWithText("标记已读").assertExists()
        composeRule.onNodeWithText("取消置顶").assertExists()
        composeRule.onNodeWithText("删除").assertExists()
        composeRule.onNodeWithText("查看详情").assertExists()
    }
}
