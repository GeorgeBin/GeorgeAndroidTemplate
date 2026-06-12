package com.georgebindragon.android.feature.main

import com.georgebindragon.android.core.appconfig.AppIcon

data class MainTabState(
    val route: String,
    val title: String,
    val icon: AppIcon,
    val selected: Boolean,
)
