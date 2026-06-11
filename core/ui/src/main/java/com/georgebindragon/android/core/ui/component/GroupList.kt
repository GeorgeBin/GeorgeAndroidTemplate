package com.georgebindragon.android.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions

data class GroupListSection<T>(
    val title: String? = null,
    val items: List<T>,
)

@Composable
fun <T> GroupList(
    sections: List<GroupListSection<T>>,
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
            section.title?.let { title ->
                item(key = "section-$title") {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            items(section.items) { item ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    itemContent(item)
                }
            }
        }
    }
}
