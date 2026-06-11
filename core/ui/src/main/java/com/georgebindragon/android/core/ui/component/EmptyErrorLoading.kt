package com.georgebindragon.android.core.ui.component

import androidx.compose.runtime.Composable

@Composable
fun EmptyErrorLoading(
    isLoading: Boolean,
    isEmpty: Boolean,
    errorMessage: String?,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    loadingContent: @Composable () -> Unit = { StatusPage(title = "Loading") },
    emptyContent: @Composable () -> Unit = { StatusPage(title = "No content") },
    errorContent: @Composable (String) -> Unit = { message -> StatusPage(title = "Error", message = message) },
    content: @Composable () -> Unit,
) {
    when {
        isLoading -> androidx.compose.foundation.layout.Box(modifier = modifier) { loadingContent() }
        errorMessage != null -> androidx.compose.foundation.layout.Box(modifier = modifier) { errorContent(errorMessage) }
        isEmpty -> androidx.compose.foundation.layout.Box(modifier = modifier) { emptyContent() }
        else -> androidx.compose.foundation.layout.Box(modifier = modifier) { content() }
    }
}
