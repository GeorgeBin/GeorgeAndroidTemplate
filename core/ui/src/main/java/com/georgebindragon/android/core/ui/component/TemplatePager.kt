package com.georgebindragon.android.core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

data class TemplatePagerPage(
    val key: String,
    val label: String,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TemplatePager(
    pages: List<TemplatePagerPage>,
    selectedPageIndex: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    pageContent: @Composable (TemplatePagerPage) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = selectedPageIndex.coerceIn(0, pages.lastIndex.coerceAtLeast(0)),
        pageCount = { pages.size },
    )

    LaunchedEffect(selectedPageIndex, pages.size) {
        if (pages.isNotEmpty() && pagerState.currentPage != selectedPageIndex) {
            pagerState.animateScrollToPage(selectedPageIndex.coerceIn(pages.indices))
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        if (pages.isNotEmpty()) {
            onPageSelected(pagerState.currentPage)
        }
    }

    Column(modifier = modifier) {
        TemplateTab(
            tabs = pages.map { TemplateTabItem(key = it.key, label = it.label) },
            selectedTabIndex = pagerState.currentPage,
            onTabSelected = onPageSelected,
        )
        HorizontalPager(state = pagerState) { page ->
            pageContent(pages[page])
        }
    }
}
