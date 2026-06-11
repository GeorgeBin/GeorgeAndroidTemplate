package com.georgebindragon.android.core.permission

sealed interface AppPermission {
    data class RuntimePermission(val androidPermission: String) : AppPermission
    data object Notification : AppPermission
    data object Overlay : AppPermission
    data object Accessibility : AppPermission
    data object IgnoreBatteryOptimization : AppPermission
    data object InstallUnknownApps : AppPermission
    data object ExactAlarm : AppPermission
    data object UsageStats : AppPermission
    data object WriteSettings : AppPermission
}

fun AppPermission.stableKey(): String = when (this) {
    is AppPermission.RuntimePermission -> "runtime:$androidPermission"
    AppPermission.Notification -> "special:notification"
    AppPermission.Overlay -> "special:overlay"
    AppPermission.Accessibility -> "special:accessibility"
    AppPermission.IgnoreBatteryOptimization -> "special:ignore_battery_optimization"
    AppPermission.InstallUnknownApps -> "special:install_unknown_apps"
    AppPermission.ExactAlarm -> "special:exact_alarm"
    AppPermission.UsageStats -> "special:usage_stats"
    AppPermission.WriteSettings -> "special:write_settings"
}
