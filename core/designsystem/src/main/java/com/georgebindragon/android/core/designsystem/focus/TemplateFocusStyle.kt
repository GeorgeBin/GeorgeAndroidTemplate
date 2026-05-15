package com.georgebindragon.android.core.designsystem.focus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class TemplateFocusStyleSet(
    val borderWidth: Dp,
    val borderColor: Color,
    val cornerRadius: Dp,
    val focusedElevation: Dp,
)

object TemplateFocusStyle {
    val current: TemplateFocusStyleSet
        @Composable
        @ReadOnlyComposable
        get() = LocalTemplateFocusStyle.current
}

internal val LocalTemplateFocusStyle = staticCompositionLocalOf {
    TemplateFocusStyleSet(
        borderWidth = 2.dp,
        borderColor = Color.Unspecified,
        cornerRadius = 8.dp,
        focusedElevation = 6.dp,
    )
}

internal fun templateFocusStyle(
    scale: Float,
    borderColor: Color,
): TemplateFocusStyleSet = TemplateFocusStyleSet(
    borderWidth = 2.dp.scaled(scale),
    borderColor = borderColor,
    cornerRadius = 8.dp.scaled(scale),
    focusedElevation = 6.dp.scaled(scale),
)

private fun Dp.scaled(scale: Float): Dp = value.times(scale).dp

