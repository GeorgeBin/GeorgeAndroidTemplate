package com.georgebindragon.android.core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions

data class StickyHeaderListSection<T>(
    val header: String,
    val items: List<T>,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> StickyHeaderList(
    sections: List<StickyHeaderListSection<T>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    itemContent: @Composable (T) -> Unit,
) {
    val dimensions = TemplateDimensions.current

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
    ) {
        sections.forEach { section ->
            stickyHeader(key = section.header) {
                Text(
                    text = section.header,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            items(section.items) { item -> itemContent(item) }
        }
    }
}
