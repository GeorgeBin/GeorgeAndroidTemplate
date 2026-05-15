package com.georgebindragon.android.core.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.georgebindragon.android.core.designsystem.focus.TemplateFocusStyle
import com.georgebindragon.android.core.input.focus.AppInteractionMode
import com.georgebindragon.android.core.ui.clickable.directionalClickable
import com.georgebindragon.android.core.ui.clickable.rememberDirectionalClickableState
import com.georgebindragon.android.core.ui.focus.LocalAppInteractionModeState

val TemplateFocusVisibleKey = SemanticsPropertyKey<Boolean>("TemplateFocusVisible")
var SemanticsPropertyReceiver.templateFocusVisible by TemplateFocusVisibleKey

@Composable
fun FocusableSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit,
) {
    val focusStyle = TemplateFocusStyle.current
    val interactionModeState = LocalAppInteractionModeState.current
    val clickableState = rememberDirectionalClickableState()
    val showFocus = enabled &&
        clickableState.focused &&
        interactionModeState.effectiveMode != AppInteractionMode.Touch

    Surface(
        modifier = modifier
            .border(
                width = if (showFocus) focusStyle.borderWidth else 0.dp,
                color = if (showFocus) focusStyle.borderColor else Color.Transparent,
                shape = RoundedCornerShape(focusStyle.cornerRadius),
            )
            .semantics {
                templateFocusVisible = showFocus
            }
            .directionalClickable(
                state = clickableState,
                enabled = enabled,
                onClick = onClick,
            ),
        shape = RoundedCornerShape(focusStyle.cornerRadius),
        color = color,
        tonalElevation = if (showFocus) focusStyle.focusedElevation else tonalElevation,
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(contentPadding),
        ) {
            content()
        }
    }
}
