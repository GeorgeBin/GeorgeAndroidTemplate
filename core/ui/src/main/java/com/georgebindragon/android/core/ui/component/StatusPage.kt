package com.georgebindragon.android.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions

@Composable
fun StatusPage(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    loading: Boolean = false,
    actions: @Composable ColumnScope.() -> Unit = {},
) {
    val dimensions = TemplateDimensions.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensions.screenHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (loading) {
            CircularProgressIndicator()
        }
        StatusPageHeader(title = title, message = message)
        actions()
    }
}

@Composable
internal fun StatusPageHeader(
    title: String,
    message: String? = null,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingSmall),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        message?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
