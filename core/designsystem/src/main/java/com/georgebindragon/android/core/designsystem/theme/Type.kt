package com.georgebindragon.android.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp

private val BaseTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
)

internal fun templateTypography(scale: Float): Typography = BaseTypography.copy(
    displayLarge = BaseTypography.displayLarge.scaled(scale),
    displayMedium = BaseTypography.displayMedium.scaled(scale),
    displaySmall = BaseTypography.displaySmall.scaled(scale),
    headlineLarge = BaseTypography.headlineLarge.scaled(scale),
    headlineMedium = BaseTypography.headlineMedium.scaled(scale),
    headlineSmall = BaseTypography.headlineSmall.scaled(scale),
    titleLarge = BaseTypography.titleLarge.scaled(scale),
    titleMedium = BaseTypography.titleMedium.scaled(scale),
    titleSmall = BaseTypography.titleSmall.scaled(scale),
    bodyLarge = BaseTypography.bodyLarge.scaled(scale),
    bodyMedium = BaseTypography.bodyMedium.scaled(scale),
    bodySmall = BaseTypography.bodySmall.scaled(scale),
    labelLarge = BaseTypography.labelLarge.scaled(scale),
    labelMedium = BaseTypography.labelMedium.scaled(scale),
    labelSmall = BaseTypography.labelSmall.scaled(scale),
)

private fun TextStyle.scaled(scale: Float): TextStyle = copy(
    fontSize = fontSize.scaled(scale),
    lineHeight = lineHeight.scaled(scale),
    letterSpacing = letterSpacing.scaled(scale),
)

private fun TextUnit.scaled(scale: Float): TextUnit = if (isSpecified) this * scale else this
