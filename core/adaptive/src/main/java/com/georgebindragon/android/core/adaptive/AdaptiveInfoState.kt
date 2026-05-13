package com.georgebindragon.android.core.adaptive

import android.content.res.Configuration
import androidx.compose.material3.adaptive.allHorizontalHingeBounds
import androidx.compose.material3.adaptive.allVerticalHingeBounds
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.separatingHorizontalHingeBounds
import androidx.compose.material3.adaptive.separatingVerticalHingeBounds
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.window.core.layout.WindowSizeClass

@Composable
fun rememberAdaptiveInfo(): AdaptiveInfo {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo(supportLargeAndXLargeWidth = true)
    val configuration = LocalConfiguration.current
    val windowSizeClass = windowAdaptiveInfo.windowSizeClass
    val posture = windowAdaptiveInfo.windowPosture

    return AdaptiveInfo(
        orientation = configuration.orientation.toAdaptiveOrientation(),
        windowWidth = windowSizeClass.toAdaptiveWindowWidth(),
        windowHeight = windowSizeClass.toAdaptiveWindowHeight(),
        posture = when {
            posture.isTabletop -> AdaptivePosture.TabletopMode
            posture.separatingVerticalHingeBounds.isNotEmpty() -> AdaptivePosture.BookMode
            posture.separatingHorizontalHingeBounds.isNotEmpty() -> AdaptivePosture.Separating
            posture.allVerticalHingeBounds.isNotEmpty() || posture.allHorizontalHingeBounds.isNotEmpty() -> {
                AdaptivePosture.Separating
            }

            else -> AdaptivePosture.Normal
        },
    )
}

private fun Int.toAdaptiveOrientation(): AdaptiveOrientation = when (this) {
    Configuration.ORIENTATION_LANDSCAPE -> AdaptiveOrientation.Landscape
    else -> AdaptiveOrientation.Portrait
}

private fun WindowSizeClass.toAdaptiveWindowWidth(): AdaptiveWindowWidth = when {
    isWidthAtLeastBreakpoint(WIDTH_DP_EXTRA_LARGE_LOWER_BOUND) -> {
        AdaptiveWindowWidth.ExtraLarge
    }

    isWidthAtLeastBreakpoint(WIDTH_DP_LARGE_LOWER_BOUND) -> {
        AdaptiveWindowWidth.Large
    }

    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
        AdaptiveWindowWidth.Expanded
    }

    isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
        AdaptiveWindowWidth.Medium
    }

    else -> AdaptiveWindowWidth.Compact
}

private fun WindowSizeClass.toAdaptiveWindowHeight(): AdaptiveWindowHeight = when {
    isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND) -> {
        AdaptiveWindowHeight.Expanded
    }

    isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND) -> {
        AdaptiveWindowHeight.Medium
    }

    else -> AdaptiveWindowHeight.Compact
}

private const val WIDTH_DP_LARGE_LOWER_BOUND = 1_200
private const val WIDTH_DP_EXTRA_LARGE_LOWER_BOUND = 1_600
