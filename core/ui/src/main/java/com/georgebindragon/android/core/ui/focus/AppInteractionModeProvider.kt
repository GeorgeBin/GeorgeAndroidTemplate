package com.georgebindragon.android.core.ui.focus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.georgebindragon.android.core.input.focus.AppInteractionMode

@Composable
fun ProvideAppInteractionMode(
    mode: AppInteractionMode,
    content: @Composable () -> Unit,
) {
    var recentInputMode by remember(mode) {
        mutableStateOf(
            when (mode) {
                AppInteractionMode.Remote -> AppInteractionMode.Remote
                AppInteractionMode.Auto,
                AppInteractionMode.Touch,
                -> AppInteractionMode.Touch
            },
        )
    }
    val state = remember(mode, recentInputMode) {
        AppInteractionModeState(
            configuredMode = mode,
            effectiveMode = when (mode) {
                AppInteractionMode.Auto -> recentInputMode
                AppInteractionMode.Touch -> AppInteractionMode.Touch
                AppInteractionMode.Remote -> AppInteractionMode.Remote
            },
            onTouchInput = {
                if (mode == AppInteractionMode.Auto) {
                    recentInputMode = AppInteractionMode.Touch
                }
            },
            onRemoteInput = {
                if (mode == AppInteractionMode.Auto) {
                    recentInputMode = AppInteractionMode.Remote
                }
            },
        )
    }

    CompositionLocalProvider(
        LocalAppInteractionModeState provides state,
        content = content,
    )
}

@Immutable
data class AppInteractionModeState(
    val configuredMode: AppInteractionMode,
    val effectiveMode: AppInteractionMode,
    val onTouchInput: () -> Unit,
    val onRemoteInput: () -> Unit,
)

val LocalAppInteractionModeState = staticCompositionLocalOf {
    AppInteractionModeState(
        configuredMode = AppInteractionMode.Auto,
        effectiveMode = AppInteractionMode.Touch,
        onTouchInput = {},
        onRemoteInput = {},
    )
}

