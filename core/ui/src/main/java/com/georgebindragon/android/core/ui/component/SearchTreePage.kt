package com.georgebindragon.android.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class SearchTreeNode<T>(
    val value: T,
    val children: List<SearchTreeNode<T>> = emptyList(),
)

data class SearchTreeItem<T>(
    val value: T,
    val depth: Int,
)

@Composable
fun <T> SearchTreePage(
    title: String,
    query: String,
    onQueryChange: (String) -> Unit,
    roots: List<SearchTreeNode<T>>,
    matchesQuery: (T, String) -> Boolean,
    modifier: Modifier = Modifier,
    searchLabel: String = "Search",
    itemContent: @Composable (SearchTreeItem<T>) -> Unit,
) {
    val visibleItems = roots
        .flatMap { it.flatten(depth = 0) }
        .filter { query.isBlank() || matchesQuery(it.value, query) }

    SearchListPage(
        title = title,
        query = query,
        onQueryChange = onQueryChange,
        items = visibleItems,
        modifier = modifier,
        searchLabel = searchLabel,
        itemContent = itemContent,
    )
}

private fun <T> SearchTreeNode<T>.flatten(depth: Int): List<SearchTreeItem<T>> =
    listOf(SearchTreeItem(value = value, depth = depth)) + children.flatMap { it.flatten(depth + 1) }
