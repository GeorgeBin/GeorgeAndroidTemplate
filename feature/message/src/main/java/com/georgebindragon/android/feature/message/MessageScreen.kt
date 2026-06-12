package com.georgebindragon.android.feature.message

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.designsystem.theme.TemplateTheme
import com.georgebindragon.android.core.ui.component.TemplateTopBar

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    uiState: MessageUiState,
    snackbarHostState: SnackbarHostState,
    onQueryChange: (String) -> Unit,
    onMarkRead: (MessageItem) -> Unit,
    onTogglePinned: (MessageItem) -> Unit,
    onDelete: (MessageItem) -> Unit,
    onShowDetail: (MessageItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current
    val messages = uiState.filteredMessages

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TemplateTopBar(title = "消息")
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = dimensions.screenHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "搜索消息") },
                singleLine = true,
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
                contentPadding = PaddingValues(bottom = dimensions.contentSpacingLarge),
            ) {
                item(key = "message-list-header") {
                    MessageListHeader(totalCount = uiState.messages.size, visibleCount = messages.size)
                }
                items(messages, key = { it.id }) { message ->
                    MessageListItem(
                        message = message,
                        onMarkRead = onMarkRead,
                        onTogglePinned = onTogglePinned,
                        onDelete = onDelete,
                        onShowDetail = onShowDetail,
                    )
                }
                item(key = "message-list-footer") {
                    MessageListFooter(hasMessages = messages.isNotEmpty())
                }
            }
        }
    }
}

@Composable
private fun MessageListHeader(
    totalCount: Int,
    visibleCount: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(TemplateDimensions.current.cardCornerRadius),
    ) {
        Text(
            text = "共 $totalCount 条消息，当前显示 $visibleCount 条",
            modifier = Modifier.padding(TemplateDimensions.current.cardPadding),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MessageListItem(
    message: MessageItem,
    onMarkRead: (MessageItem) -> Unit,
    onTogglePinned: (MessageItem) -> Unit,
    onDelete: (MessageItem) -> Unit,
    onShowDetail: (MessageItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current
    var menuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { onShowDetail(message) },
                    onLongClick = { menuExpanded = true },
                ),
            shape = RoundedCornerShape(dimensions.cardCornerRadius),
            tonalElevation = dimensions.cardElevation,
            color = if (message.read) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            },
        ) {
            Column(
                modifier = Modifier.padding(dimensions.cardPadding),
                verticalArrangement = Arrangement.spacedBy(dimensions.cardContentSpacing),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = message.sender,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = message.time,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensions.contentSpacingSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = message.title,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (message.read) FontWeight.Normal else FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (message.pinned) {
                        Text(
                            text = "置顶",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Text(
                    text = message.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(text = "标记已读") },
                onClick = {
                    menuExpanded = false
                    onMarkRead(message)
                },
            )
            DropdownMenuItem(
                text = { Text(text = if (message.pinned) "取消置顶" else "置顶") },
                onClick = {
                    menuExpanded = false
                    onTogglePinned(message)
                },
            )
            DropdownMenuItem(
                text = { Text(text = "删除") },
                onClick = {
                    menuExpanded = false
                    onDelete(message)
                },
            )
            DropdownMenuItem(
                text = { Text(text = "查看详情") },
                onClick = {
                    menuExpanded = false
                    onShowDetail(message)
                },
            )
        }
    }
}

@Composable
private fun MessageListFooter(
    hasMessages: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = if (hasMessages) "已显示全部消息" else "没有找到匹配的消息",
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = TemplateDimensions.current.contentSpacingMedium),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview(showBackground = true)
@Composable
private fun MessageScreenPreview() {
    TemplateTheme {
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
