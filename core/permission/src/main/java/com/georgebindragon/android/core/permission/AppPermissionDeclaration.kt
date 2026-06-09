package com.georgebindragon.android.core.permission

import com.georgebindragon.android.base.common.UiText

data class AppPermissionDeclaration(
    val permission: AppPermission,
    val title: UiText,
    val description: UiText,
    val required: Boolean,
    val requestTiming: PermissionRequestTiming,
)

enum class PermissionRequestTiming {
    OnStartup,
    BeforeLogin,
    AfterLogin,
    OnFeatureUse,
    ManualOnly,
}
