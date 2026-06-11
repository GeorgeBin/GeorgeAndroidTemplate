package com.georgebindragon.android.feature.timedebug

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.georgebindragon.android.core.time.AppTimeState
import com.georgebindragon.android.core.ui.component.FormPage

@Composable
fun TimeDebugScreen(
    state: AppTimeState,
    modifier: Modifier = Modifier,
) {
    FormPage(
        title = "时间状态",
        subtitle = "展示 App 时间、开机时长、时区和日期时间拆分。",
        modifier = modifier,
    ) {
        DebugLine(label = "System millis", value = state.systemTimeMillis.toString())
        DebugLine(label = "Elapsed millis", value = state.elapsedRealtimeMillis.toString())
        DebugLine(label = "Zone", value = state.zoneId.id)
        DebugLine(label = "Date", value = state.date.toString())
        DebugLine(label = "Time", value = "%02d:%02d:%02d".format(state.hour, state.minute, state.second))
    }
}

@Composable
private fun DebugLine(
    label: String,
    value: String,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
