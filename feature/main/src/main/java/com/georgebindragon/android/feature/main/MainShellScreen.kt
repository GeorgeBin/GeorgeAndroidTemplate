package com.georgebindragon.android.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.georgebindragon.android.core.appconfig.AppIcon
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.ui.component.FocusableSurface
import com.georgebindragon.android.feature.main.R as MainR

@Composable
fun MainShellScreen(
    tabs: List<MainTabState>,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            MainBottomBar(
                tabs = tabs,
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
                    MainBottomTab(
                        tab = tab,
                        onClick = {
                            if (!tab.selected) {
                                onTabClick(tab.route)
                            }
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun MainBottomTab(
    tab: MainTabState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current
    val contentColor = if (tab.selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    FocusableSurface(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = dimensions.footerTabMinHeight)
            .semantics {
                contentDescription = tab.title
            },
        color = MaterialTheme.colorScheme.surface,
        contentPadding = PaddingValues(
            horizontal = dimensions.footerTabHorizontalPadding,
            vertical = dimensions.footerTabVerticalPadding,
        ),
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensions.footerTabContentSpacing),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(
                    painter = painterResource(id = tab.icon.drawableResId()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensions.footerTabIconSize)
                        .align(Alignment.CenterHorizontally),
                )
                Text(
                    text = tab.title,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }
    }
}

private fun AppIcon.drawableResId(): Int = when (this) {
    AppIcon.Home -> MainR.drawable.ic_tab_home
    AppIcon.Message -> MainR.drawable.ic_tab_message
    AppIcon.Workbench -> MainR.drawable.ic_tab_workbench
    AppIcon.Settings -> MainR.drawable.ic_tab_settings
    AppIcon.Placeholder -> MainR.drawable.ic_tab_placeholder
}
