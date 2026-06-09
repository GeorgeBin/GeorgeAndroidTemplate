package com.georgebindragon.android.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.georgebindragon.android.base.common.UiText
import com.georgebindragon.android.core.appconfig.TabConfig

@Composable
fun MainShellRoute(
    tabs: List<TabConfig>,
    selectedRoute: String?,
    appName: String,
    packageName: String,
    versionName: String,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    MainShellScreen(
        tabs = tabs
            .filter { it.visible }
            .sortedBy { it.order }
            .map { tab ->
                MainTabState(
                    route = tab.route,
                    title = tab.title.resolve(),
                    selected = tab.route == selectedRoute,
                )
            },
        appName = appName,
        packageName = packageName,
        versionName = versionName,
        onTabClick = onTabClick,
        modifier = modifier,
        content = content,
    )
}

@Composable
private fun UiText.resolve(): String = when (this) {
    is UiText.Plain -> value
    is UiText.Resource -> stringResource(id = resId, *args.toTypedArray())
}
