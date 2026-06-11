package com.georgebindragon.android.core.appconfig

import com.georgebindragon.android.base.common.UiText
import com.georgebindragon.android.core.permission.AppPermission
import com.georgebindragon.android.core.permission.AppPermissionDeclaration
import com.georgebindragon.android.core.permission.PermissionRequestTiming

data class AppConfig(
    val privacy: PrivacyFeatureConfig = PrivacyFeatureConfig(),
    val permission: PermissionFeatureConfig = PermissionFeatureConfig(),
    val auth: AuthFeatureConfig = AuthFeatureConfig(),
    val main: MainFeatureConfig = MainFeatureConfig(),
    val settings: SettingsFeatureConfig = SettingsFeatureConfig(),
    val business: Map<String, BusinessFeatureConfig> = emptyMap(),
)

data class PrivacyFeatureConfig(
    val enabled: Boolean = true,
    val privacyVersion: Int = 1,
    val userAgreementVersion: Int = 1,
)

data class PermissionFeatureConfig(
    val enabled: Boolean = true,
    val showOverviewOnFirstLaunch: Boolean = true,
    val overviewVersion: Int = 1,
    val allowSkipOptional: Boolean = true,
    val allowSkipRequired: Boolean = false,
    val declarations: List<AppPermissionDeclaration> = DefaultPermissionDeclarations,
)

data class AuthFeatureConfig(
    val enabled: Boolean = true,
    val required: Boolean = true,
    val allowGuestMode: Boolean = false,
)

data class MainFeatureConfig(
    val enabled: Boolean = true,
    val tabs: List<TabConfig> = DefaultTabs,
)

data class SettingsFeatureConfig(
    val enabled: Boolean = true,
    val showLanguage: Boolean = true,
    val showTheme: Boolean = true,
    val showAppScale: Boolean = true,
    val showPageOrientation: Boolean = true,
    val showExpertMode: Boolean = true,
    val showPermission: Boolean = true,
    val showPrivacy: Boolean = true,
    val showAbout: Boolean = true,
    val showLogout: Boolean = true,
)

data class BusinessFeatureConfig(
    val featureKey: String,
    val enabled: Boolean,
    val requiresLogin: Boolean = false,
    val requiredPermissions: List<AppPermission> = emptyList(),
)

data class TabConfig(
    val route: String,
    val title: UiText,
    val icon: AppIcon,
    val order: Int,
    val visible: Boolean = true,
    val requiresLogin: Boolean = false,
    val requiredPermissions: List<AppPermission> = emptyList(),
)

enum class AppIcon {
    Home,
    Message,
    Workbench,
    Settings,
    Placeholder,
}

val DefaultTabs: List<TabConfig> = listOf(
    TabConfig(
        route = "home",
        title = UiText.Plain("首页"),
        icon = AppIcon.Home,
        order = 0,
    ),
    TabConfig(
        route = "settings",
        title = UiText.Plain("设置"),
        icon = AppIcon.Settings,
        order = 10,
    ),
)

val DefaultPermissionDeclarations: List<AppPermissionDeclaration> = listOf(
    AppPermissionDeclaration(
        permission = AppPermission.Notification,
        title = UiText.Plain("通知权限"),
        description = UiText.Plain("用于展示应用运行状态、提醒和后台任务通知。"),
        required = false,
        requestTiming = PermissionRequestTiming.OnStartup,
    ),
)
