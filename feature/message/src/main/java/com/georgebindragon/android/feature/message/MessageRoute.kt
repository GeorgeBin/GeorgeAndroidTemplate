package com.georgebindragon.android.feature.message

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun MessageRoute(
    modifier: Modifier = Modifier,
) {
    var uiState by remember { mutableStateOf(MessageUiState()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun showMessage(text: String) {
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }
    }

    MessageScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onQueryChange = { query ->
            uiState = uiState.copy(query = query)
        },
        onMarkRead = { message ->
            uiState = uiState.copy(
                messages = uiState.messages.map {
                    if (it.id == message.id) it.copy(read = true) else it
                },
            )
            showMessage("已标记为已读：${message.title}")
        },
        onTogglePinned = { message ->
            val nextPinned = !message.pinned
            uiState = uiState.copy(
                messages = uiState.messages.map {
                    if (it.id == message.id) it.copy(pinned = nextPinned) else it
                },
            )
            showMessage(if (nextPinned) "已置顶：${message.title}" else "已取消置顶：${message.title}")
        },
        onDelete = { message ->
            uiState = uiState.copy(
                messages = uiState.messages.filterNot { it.id == message.id },
            )
            showMessage("已删除：${message.title}")
        },
        onShowDetail = { message ->
            showMessage("查看详情：${message.title}")
        },
        modifier = modifier,
    )
}
