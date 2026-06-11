package com.georgebindragon.android.core.permission

data class PermissionState(
    val declaration: AppPermissionDeclaration,
    val granted: Boolean,
    val skipped: Boolean = false,
)

data class PermissionGateState(
    val permissions: List<PermissionState> = emptyList(),
) {
    val missingRequired: List<PermissionState>
        get() = permissions.filter { it.declaration.required && !it.granted }

    val missingOptional: List<PermissionState>
        get() = permissions.filter { !it.declaration.required && !it.granted && !it.skipped }

    val hasMissingPermissions: Boolean
        get() = missingRequired.isNotEmpty() || missingOptional.isNotEmpty()
}
