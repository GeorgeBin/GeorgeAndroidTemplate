package com.georgebindragon.android.core.navigation

object RootRoute {
    const val Startup = "startup"
    const val Main = "main"
}

object StartupRoute {
    const val Privacy = "startup/privacy"
    const val PermissionOverview = "startup/permission-overview"
    const val PermissionRequest = "startup/permission-request"
    const val Login = "startup/login"
}

object MainRoute {
    const val Shell = "main/shell"
    const val Home = "home"
    const val Settings = "settings"
}
