package com.georgebindragon.android.core.ui.clickable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import com.georgebindragon.android.core.input.keyevent.InputKey
import com.georgebindragon.android.core.ui.focus.LocalAppInteractionModeState
import com.georgebindragon.android.core.ui.keyevent.toInputKey

@Stable
data class DirectionalClickableState(
    val interactionSource: MutableInteractionSource,
    val focused: Boolean,
    val debouncedClickState: DebouncedClickState,
)

@Composable
fun rememberDirectionalClickableState(
    debounceIntervalMillis: Long = DefaultClickDebounceIntervalMillis,
): DirectionalClickableState {
    val interactionSource = remember { MutableInteractionSource() }
    val debouncedClickState = remember(debounceIntervalMillis) {
        DebouncedClickState(intervalMillis = debounceIntervalMillis)
    }
    val focused by interactionSource.collectIsFocusedAsState()
    return DirectionalClickableState(
        interactionSource = interactionSource,
        focused = focused,
        debouncedClickState = debouncedClickState,
    )
}

@Composable
fun Modifier.directionalClickable(
    state: DirectionalClickableState,
    onClick: () -> Unit,
    enabled: Boolean = true,
): Modifier {
    val interactionModeState = LocalAppInteractionModeState.current
    val indication = LocalIndication.current
    return this
        .pointerInput(enabled, interactionModeState) {
            if (!enabled) return@pointerInput
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent()
                    interactionModeState.onTouchInput()
                }
            }
        }
        .onPreviewKeyEvent { event ->
            val inputKey = event.toInputKey()
            if (inputKey != null) {
                interactionModeState.onRemoteInput()
            }
            if (enabled && event.type == KeyEventType.KeyDown && inputKey == InputKey.Confirm) {
                state.debouncedClickState.tryClick(onClick)
                true
            } else {
                false
            }
        }
        .focusable(
            enabled = enabled,
            interactionSource = state.interactionSource,
        )
        .clickable(
            enabled = enabled,
            interactionSource = state.interactionSource,
            indication = indication,
            onClick = {
                interactionModeState.onTouchInput()
                state.debouncedClickState.tryClick(onClick)
            },
        )
}
