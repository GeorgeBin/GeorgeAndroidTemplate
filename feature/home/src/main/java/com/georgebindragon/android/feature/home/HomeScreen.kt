package com.georgebindragon.android.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.designsystem.theme.TemplateTheme
import com.georgebindragon.android.core.model.HomeItem

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onExitClick: () -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val dimensions = TemplateDimensions.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .padding(
                horizontal = dimensions.screenHorizontalPadding,
                vertical = dimensions.screenVerticalPadding,
            )
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingSmall),
    ) {
        Text(
            text = stringResource(R.string.home_content_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = stringResource(R.string.home_content_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        when (uiState) {
            HomeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensions.contentSpacingLarge),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is HomeUiState.Success -> {
                uiState.items.forEach { item ->
                    HomeItemCard(item = item)
                }
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensions.contentSpacingMedium),
        ) {
            val buttonWidth = (maxWidth - dimensions.contentSpacingMedium) / 2

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
            ) {
                Button(
                    onClick = onExitClick,
                    modifier = Modifier.width(buttonWidth),
                ) {
                    Text(text = stringResource(R.string.home_exit_app))
                }

                Button(
                    onClick = onRestartClick,
                    modifier = Modifier.width(buttonWidth),
                ) {
                    Text(text = stringResource(R.string.home_restart_app))
                }
            }
        }
    }
}

@Composable
private fun HomeItemCard(
    item: HomeItem,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensions.cardCornerRadius),
        tonalElevation = dimensions.cardElevation,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = Modifier.padding(dimensions.cardPadding),
            verticalArrangement = Arrangement.spacedBy(dimensions.cardContentSpacing),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = item.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    TemplateTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreen(
                uiState = HomeUiState.Success(
                    items = listOf(
                        HomeItem(
                            id = "preview",
                            title = "内容模块 1",
                            body = "这里预留业务内容展示区域。",
                        ),
                    ),
                ),
                onExitClick = {},
                onRestartClick = {},
            )
        }
    }
}
