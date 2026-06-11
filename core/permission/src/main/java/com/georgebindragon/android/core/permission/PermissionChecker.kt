package com.georgebindragon.android.core.permission

import android.Manifest
import android.app.AlarmManager
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

interface PermissionChecker {
    fun isGranted(permission: AppPermission): Boolean
}

class AndroidPermissionChecker(
    private val context: Context,
) : PermissionChecker {
    override fun isGranted(permission: AppPermission): Boolean = when (permission) {
        is AppPermission.RuntimePermission -> ContextCompat.checkSelfPermission(
            context,
            permission.androidPermission,
        ) == PackageManager.PERMISSION_GRANTED

        AppPermission.Notification -> {
            val notificationEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationEnabled && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                notificationEnabled
            }
        }

        AppPermission.Overlay -> Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            Settings.canDrawOverlays(context)

        AppPermission.Accessibility -> false

        AppPermission.IgnoreBatteryOptimization -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                true
            } else {
                val powerManager = context.getSystemService(PowerManager::class.java)
                powerManager?.isIgnoringBatteryOptimizations(context.packageName) == true
            }
        }

        AppPermission.InstallUnknownApps -> Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
            context.packageManager.canRequestPackageInstalls()

        AppPermission.ExactAlarm -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                true
            } else {
                val alarmManager = context.getSystemService(AlarmManager::class.java)
                alarmManager?.canScheduleExactAlarms() == true
            }
        }

        AppPermission.UsageStats -> isAppOpsAllowed(AppOpsManager.OPSTR_GET_USAGE_STATS)
        AppPermission.WriteSettings -> Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            Settings.System.canWrite(context)
    }

    private fun isAppOpsAllowed(op: String): Boolean {
        val appOpsManager = context.getSystemService(AppOpsManager::class.java) ?: return false
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(op, context.applicationInfo.uid, context.packageName)
        } else {
            @Suppress("DEPRECATION")
            appOpsManager.checkOpNoThrow(op, context.applicationInfo.uid, context.packageName)
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }
}

internal fun Context.packageSettingsUri(): Uri = Uri.parse("package:$packageName")
