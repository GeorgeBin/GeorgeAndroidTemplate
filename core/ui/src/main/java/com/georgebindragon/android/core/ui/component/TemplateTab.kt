package com.georgebindragon.android.core.ui.component

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class TemplateTabItem(
    val key: String,
    val label: String,
)

@Composable
fun TemplateTab(
    tabs: List<TemplateTabItem>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (tabs.isEmpty()) return

    TabRow(
        selectedTabIndex = selectedTabIndex.coerceIn(tabs.indices),
        modifier = modifier,
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = { onTabSelected(index) },
                text = { Text(text = tab.label) },
            )
        }
    }
}
