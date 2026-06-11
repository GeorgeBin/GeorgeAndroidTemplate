package com.georgebindragon.android.feature.diagnostics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.georgebindragon.android.base.common.UiText
import com.georgebindragon.android.base.networktool.PingResult
import com.georgebindragon.android.base.shell.ShellResult
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.lifecycle.AppForegroundState
import com.georgebindragon.android.core.lifecycle.AppStartSource
import com.georgebindragon.android.core.permission.PermissionGateState
import com.georgebindragon.android.core.ui.component.FocusableButton
import com.georgebindragon.android.core.ui.component.FocusableSurface
import com.georgebindragon.android.core.ui.component.FormPage
import com.georgebindragon.android.core.ui.component.GroupList
import com.georgebindragon.android.core.ui.component.GroupListSection

data class DiagnosticEntry(
    val title: String,
    val description: String,
    val onClick: () -> Unit,
)

@Composable
fun DiagnosticsScreen(
    entries: List<DiagnosticEntry>,
    permissionGateState: PermissionGateState?,
    appStartSource: AppStartSource?,
    foregroundState: AppForegroundState?,
    shellCommand: String,
    onShellCommandChange: (String) -> Unit,
    shellResult: ShellResult?,
    onRunShellClick: () -> Unit,
    pingHost: String,
    onPingHostChange: (String) -> Unit,
    pingResult: PingResult?,
    onRunPingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FormPage(
        title = "诊断",
        subtitle = "用于验证模板能力的可选调试页面。",
        modifier = modifier,
    ) {
        GroupList(
            sections = listOf(GroupListSection(title = "页面", items = entries)),
        ) { entry ->
            DiagnosticEntryRow(entry = entry)
        }
        PermissionStatusSection(permissionGateState = permissionGateState)
        LifecycleStatusSection(
            appStartSource = appStartSource,
            foregroundState = foregroundState,
        )
        ShellTestSection(
            command = shellCommand,
            onCommandChange = onShellCommandChange,
            result = shellResult,
            onRunClick = onRunShellClick,
        )
        PingTestSection(
            host = pingHost,
            onHostChange = onPingHostChange,
            result = pingResult,
            onRunClick = onRunPingClick,
        )
    }
}

@Composable
private fun DiagnosticEntryRow(
    entry: DiagnosticEntry,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current

    FocusableSurface(
        onClick = entry.onClick,
        modifier = modifier.fillMaxWidth(),
        tonalElevation = dimensions.settingsRowElevation,
        contentPadding = PaddingValues(
            horizontal = dimensions.settingsRowHorizontalPadding,
            vertical = dimensions.settingsRowVerticalPadding,
        ),
    ) {
        Column {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PermissionStatusSection(permissionGateState: PermissionGateState?) {
    DebugSection(title = "权限状态") {
        if (permissionGateState == null) {
            DebugLine(label = "状态", value = "未绑定")
        } else {
            permissionGateState.permissions.forEach { state ->
                DebugLine(
                    label = state.declaration.title.asText(),
                    value = when {
                        state.granted -> "已授权"
                        state.skipped -> "已跳过"
                        state.declaration.required -> "必要，未授权"
                        else -> "可选，未授权"
                    },
                )
            }
        }
    }
}

@Composable
private fun LifecycleStatusSection(
    appStartSource: AppStartSource?,
    foregroundState: AppForegroundState?,
) {
    DebugSection(title = "生命周期状态") {
        DebugLine(label = "启动来源", value = appStartSource?.label() ?: "未绑定")
        DebugLine(label = "前后台", value = foregroundState?.label() ?: "未绑定")
    }
}

@Composable
private fun ShellTestSection(
    command: String,
    onCommandChange: (String) -> Unit,
    result: ShellResult?,
    onRunClick: () -> Unit,
) {
    DebugSection(title = "Shell 测试") {
        OutlinedTextField(
            value = command,
            onValueChange = onCommandChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Command") },
            singleLine = true,
        )
        FocusableButton(onClick = onRunClick) {
            Text(text = "执行")
        }
        result?.let {
            DebugLine(label = "Exit", value = it.exitCode?.toString() ?: "无")
            DebugLine(label = "Output", value = it.standardOutput.ifBlank { "(empty)" })
            DebugLine(label = "Error", value = it.standardError.ifBlank { "(empty)" })
        }
    }
}

@Composable
private fun PingTestSection(
    host: String,
    onHostChange: (String) -> Unit,
    result: PingResult?,
    onRunClick: () -> Unit,
) {
    DebugSection(title = "Ping 测试") {
        OutlinedTextField(
            value = host,
            onValueChange = onHostChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Host") },
            singleLine = true,
        )
        FocusableButton(onClick = onRunClick) {
            Text(text = "测试")
        }
        result?.let {
            DebugLine(label = "Host", value = it.host)
            DebugLine(label = "Received", value = "${it.receivedCount}/${it.transmittedCount}")
            DebugLine(label = "Loss", value = "${it.packetLossPercent}%")
            DebugLine(label = "Avg", value = it.averageLatencyMillis?.let { millis -> "${millis}ms" } ?: "无")
            it.errorMessage?.let { message -> DebugLine(label = "Error", value = message) }
        }
    }
}

@Composable
private fun DebugSection(
    title: String,
    content: @Composable () -> Unit,
) {
    val dimensions = TemplateDimensions.current

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensions.contentSpacingSmall),
        ) {
            content()
        }
    }
}

@Composable
private fun DebugLine(
    label: String,
    value: String,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = value,
            modifier = Modifier.weight(2f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun UiText.asText(): String = when (this) {
    is UiText.Plain -> value
    is UiText.Resource -> stringResource(resId, *args.toTypedArray())
}

private fun AppStartSource.label(): String = when (this) {
    AppStartSource.BootCompleted -> "开机启动"
    AppStartSource.DeepLink -> "深链"
    AppStartSource.Launcher -> "桌面启动"
    AppStartSource.Notification -> "通知"
    AppStartSource.Service -> "服务"
    is AppStartSource.Unknown -> "未知 $reason"
}

private fun AppForegroundState.label(): String = when (this) {
    AppForegroundState.Background -> "后台"
    AppForegroundState.Foreground -> "前台"
}
