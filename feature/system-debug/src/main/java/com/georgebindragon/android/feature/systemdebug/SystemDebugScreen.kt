package com.georgebindragon.android.feature.systemdebug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.georgebindragon.android.core.designsystem.theme.TemplateDimensions
import com.georgebindragon.android.core.system.SystemCapabilities
import com.georgebindragon.android.core.system.SystemCapability
import com.georgebindragon.android.core.ui.component.FocusableSurface
import com.georgebindragon.android.core.ui.component.FormPage

@Composable
fun SystemDebugScreen(
    capabilities: SystemCapabilities,
    modifier: Modifier = Modifier,
) {
    FormPage(
        title = "系统能力检测",
        subtitle = "展示系统签名、Root 或特权执行相关能力是否可用。",
        modifier = modifier,
    ) {
        SystemCapability.entries.forEach { capability ->
            CapabilityRow(
                capability = capability,
                capabilities = capabilities,
            )
        }
    }
}

@Composable
private fun CapabilityRow(
    capability: SystemCapability,
    capabilities: SystemCapabilities,
    modifier: Modifier = Modifier,
) {
    val dimensions = TemplateDimensions.current
    val status = capabilities.statusOf(capability)

    FocusableSurface(
        onClick = {},
        modifier = modifier.fillMaxWidth(),
        tonalElevation = dimensions.settingsRowElevation,
        contentPadding = PaddingValues(
            horizontal = dimensions.settingsRowHorizontalPadding,
            vertical = dimensions.settingsRowVerticalPadding,
        ),
    ) {
        Column {
            Text(
                text = capability.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = if (status.available) "可用" else "不可用：${status.reason.orEmpty()}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (status.available) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}
