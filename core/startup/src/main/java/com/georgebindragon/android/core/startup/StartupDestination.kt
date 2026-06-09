package com.georgebindragon.android.core.startup

sealed interface StartupDestination {
    data object Privacy : StartupDestination
    data object PermissionOverview : StartupDestination
    data object Login : StartupDestination
    data object Main : StartupDestination
}
