package com.georgebindragon.android.core.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

interface PermissionIntentFactory {
    fun createIntent(permission: AppPermission): Intent?
}

class AndroidPermissionIntentFactory(
    private val context: Context,
) : PermissionIntentFactory {
    override fun createIntent(permission: AppPermission): Intent? = when (permission) {
        is AppPermission.RuntimePermission -> null
        AppPermission.Notification -> notificationIntent()
        AppPermission.Overlay -> Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            context.packageSettingsUri(),
        )
        AppPermission.Accessibility -> Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        AppPermission.IgnoreBatteryOptimization -> Intent(
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            context.packageSettingsUri(),
        )
        AppPermission.InstallUnknownApps -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, context.packageSettingsUri())
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
        AppPermission.ExactAlarm -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, context.packageSettingsUri())
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, context.packageSettingsUri())
        }
        AppPermission.UsageStats -> Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        AppPermission.WriteSettings -> Intent(
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            context.packageSettingsUri(),
        )
    }?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    private fun notificationIntent(): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        } else {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context.packageName}"))
        }
    }
}

fun AppPermission.runtimePermissionName(): String? = when (this) {
    is AppPermission.RuntimePermission -> androidPermission
    AppPermission.Notification -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        null
    }
    else -> null
}
