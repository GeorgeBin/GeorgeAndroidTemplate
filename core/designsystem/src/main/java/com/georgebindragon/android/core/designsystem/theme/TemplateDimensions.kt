package com.georgebindragon.android.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class TemplateDimensionSet(
    val screenHorizontalPadding: Dp,
    val screenVerticalPadding: Dp,
    val contentSpacingSmall: Dp,
    val contentSpacingMedium: Dp,
    val contentSpacingLarge: Dp,
    val cardCornerRadius: Dp,
    val cardPadding: Dp,
    val cardContentSpacing: Dp,
    val cardElevation: Dp,
    val settingsRowHorizontalPadding: Dp,
    val settingsRowVerticalPadding: Dp,
    val settingsRowElevation: Dp,
    val footerElevation: Dp,
    val footerNavHorizontalPadding: Dp,
    val footerInfoHorizontalPadding: Dp,
    val footerVerticalPadding: Dp,
)

object TemplateDimensions {
    val current: TemplateDimensionSet
        @Composable
        @ReadOnlyComposable
        get() = LocalTemplateDimensions.current
}

internal val LocalTemplateDimensions = staticCompositionLocalOf {
    templateDimensions(scale = 1.0f)
}

internal fun templateDimensions(scale: Float): TemplateDimensionSet = TemplateDimensionSet(
    screenHorizontalPadding = 20.dp.scaled(scale),
    screenVerticalPadding = 4.dp.scaled(scale),
    contentSpacingSmall = 4.dp.scaled(scale),
    contentSpacingMedium = 12.dp.scaled(scale),
    contentSpacingLarge = 48.dp.scaled(scale),
    cardCornerRadius = 8.dp.scaled(scale),
    cardPadding = 20.dp.scaled(scale),
    cardContentSpacing = 10.dp.scaled(scale),
    cardElevation = 3.dp.scaled(scale),
    settingsRowHorizontalPadding = 16.dp.scaled(scale),
    settingsRowVerticalPadding = 12.dp.scaled(scale),
    settingsRowElevation = 2.dp.scaled(scale),
    footerElevation = 8.dp.scaled(scale),
    footerNavHorizontalPadding = 12.dp.scaled(scale),
    footerInfoHorizontalPadding = 20.dp.scaled(scale),
    footerVerticalPadding = 2.dp.scaled(scale),
)

private fun Dp.scaled(scale: Float): Dp = value.times(scale).dp
