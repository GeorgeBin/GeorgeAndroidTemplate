package com.georgebindragon.android.feature.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.ui.component.FocusableTextButton

@Composable
fun MainShellScreen(
    tabs: List<MainTabState>,
    appName: String,
    packageName: String,
    versionName: String,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            MainBottomBar(
                tabs = tabs,
                appName = appName,
                packageName = packageName,
                versionName = versionName,
                onTabClick = onTabClick,
            )
        },
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Composable
private fun MainBottomBar(
    tabs: List<MainTabState>,
    appName: String,
    packageName: String,
    versionName: String,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = dimensions.footerElevation,
        shadowElevation = dimensions.footerElevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
        ) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.footerNavHorizontalPadding,
                        vertical = dimensions.footerVerticalPadding,
                    ),
            ) {
                tabs.forEach { tab ->
                    FocusableTextButton(
                        onClick = { onTabClick(tab.route) },
                        enabled = !tab.selected,
                    ) {
                        Text(text = tab.title)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensions.footerInfoHorizontalPadding,
                        vertical = dimensions.footerVerticalPadding,
                    ),
            ) {
                Text(
                    text = appName,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "$packageName  v$versionName",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}
