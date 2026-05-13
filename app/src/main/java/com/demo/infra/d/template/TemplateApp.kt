package com.demo.infra.d.template

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
import androidx.compose.ui.unit.dp
import com.demo.infra.d.template.feature.home.HomeRoute

@Composable
fun TemplateApp(
    appName: String,
    packageName: String,
    versionName: String,
    onExitClick: () -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            AppFooter(
                appName = appName,
                packageName = packageName,
                versionName = versionName,
            )
        },
    ) { innerPadding ->
        TemplateNavHost(
            onExitClick = onExitClick,
            onRestartClick = onRestartClick,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun TemplateNavHost(
    onExitClick: () -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeRoute(
        onExitClick = onExitClick,
        onRestartClick = onRestartClick,
        modifier = modifier,
    )
}

@Composable
private fun AppFooter(
    appName: String,
    packageName: String,
    versionName: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
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
                    .padding(horizontal = 20.dp, vertical = 2.dp),
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
