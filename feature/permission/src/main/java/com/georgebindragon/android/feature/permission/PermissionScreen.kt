package com.georgebindragon.android.feature.permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.georgebindragon.android.base.common.UiText
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.permission.AppPermission
import com.georgebindragon.android.core.permission.PermissionGateState
import com.georgebindragon.android.core.permission.PermissionState
import com.georgebindragon.android.core.permission.runtimePermissionName
import com.georgebindragon.android.core.ui.component.FocusableButton
import com.georgebindragon.android.core.ui.component.FocusableSurface

@Composable
fun PermissionOverviewScreen(
    gateState: PermissionGateState,
    allowSkipOptional: Boolean,
    onContinueClick: () -> Unit,
    onSkipOptionalClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PermissionScaffold(
        title = "权限总览",
        description = "以下权限会影响模板应用的部分能力，可按需授予。",
        gateState = gateState,
        modifier = modifier,
    ) {
        FocusableButton(onClick = onContinueClick) {
            Text(text = if (gateState.hasMissingPermissions) "继续" else "进入应用")
        }
        if (allowSkipOptional && gateState.missingRequired.isEmpty() && gateState.missingOptional.isNotEmpty()) {
            FocusableButton(onClick = onSkipOptionalClick) {
                Text(text = "跳过可选权限")
            }
        }
    }
}

@Composable
fun PermissionRequestScreen(
    gateState: PermissionGateState,
    allowSkipOptional: Boolean,
    allowSkipRequired: Boolean,
    onRequestRuntimePermissionsClick: () -> Unit,
    onOpenSettingsClick: (AppPermission) -> Unit,
    onRefreshClick: () -> Unit,
    onSkipOptionalClick: () -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val missingRuntimePermissions = gateState.permissions
        .filter { !it.granted && it.declaration.permission.runtimePermissionName() != null }
    val missingSpecialPermissions = gateState.permissions
        .filter { !it.granted && it.declaration.permission.runtimePermissionName() == null }

    PermissionScaffold(
        title = "权限申请",
        description = "授予必要权限后继续；可选权限可以稍后在设置中处理。",
        gateState = gateState,
        modifier = modifier,
    ) {
        if (missingRuntimePermissions.isNotEmpty()) {
            FocusableButton(onClick = onRequestRuntimePermissionsClick) {
                Text(text = "申请普通权限")
            }
        }
        missingSpecialPermissions.forEach { state ->
            FocusableButton(
                onClick = { onOpenSettingsClick(state.declaration.permission) },
            ) {
                Text(text = "打开${state.declaration.title.asText()}")
            }
        }
        FocusableButton(onClick = onRefreshClick) {
            Text(text = "刷新权限状态")
        }
        if (allowSkipOptional && gateState.missingRequired.isEmpty() && gateState.missingOptional.isNotEmpty()) {
            FocusableButton(onClick = onSkipOptionalClick) {
                Text(text = "跳过可选权限")
            }
        }
        if (allowSkipRequired || gateState.missingRequired.isEmpty()) {
            FocusableButton(onClick = onCompleteClick) {
                Text(text = "完成")
            }
        }
    }
}

@Composable
private fun PermissionScaffold(
    title: String,
    description: String,
    gateState: PermissionGateState,
    modifier: Modifier = Modifier,
    actions: @Composable ColumnScope.() -> Unit,
) {
    val dimensions = TemplateDimensions.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = dimensions.screenHorizontalPadding,
                vertical = dimensions.screenVerticalPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        gateState.permissions.forEach { state ->
            PermissionRow(state = state)
        }
        actions()
    }
}

@Composable
private fun PermissionRow(
    state: PermissionState,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current
    val status = when {
        state.granted -> "已授权"
        state.skipped -> "已跳过"
        state.declaration.required -> "必要"
        else -> "可选"
    }

    FocusableSurface(
        onClick = {},
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = dimensions.settingsRowElevation,
        contentPadding = PaddingValues(
            horizontal = dimensions.settingsRowHorizontalPadding,
            vertical = dimensions.settingsRowVerticalPadding,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.contentSpacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.declaration.title.asText(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = state.declaration.description.asText(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = status,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun UiText.asText(): String = when (this) {
    is UiText.Plain -> value
    is UiText.Resource -> stringResource(resId, *args.toTypedArray())
}
