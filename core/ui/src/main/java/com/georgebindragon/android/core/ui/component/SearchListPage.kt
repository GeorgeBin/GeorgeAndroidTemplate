package com.georgebindragon.android.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions

@Composable
fun <T> SearchListPage(
    title: String,
    query: String,
    onQueryChange: (String) -> Unit,
    items: List<T>,
    modifier: Modifier = Modifier,
    searchLabel: String = "Search",
    emptyContent: @Composable () -> Unit = { StatusPage(title = "No results") },
    itemContent: @Composable (T) -> Unit,
) {
    val dimensions = TemplateDimensions.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(
                horizontal = dimensions.screenHorizontalPadding,
                vertical = dimensions.screenVerticalPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
    ) {
        StatusPageHeader(title = title)
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = searchLabel) },
            singleLine = true,
        )
        if (items.isEmpty()) {
            emptyContent()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
                contentPadding = PaddingValues(bottom = dimensions.contentSpacingLarge),
            ) {
                items(items) { item -> itemContent(item) }
            }
        }
    }
}
