package com.georgebindragon.android.core.adaptive

data class AdaptiveInfo(
    val orientation: AdaptiveOrientation,
    val windowWidth: AdaptiveWindowWidth,
    val windowHeight: AdaptiveWindowHeight,
    val posture: AdaptivePosture,
) {
    val isPortrait: Boolean = orientation == AdaptiveOrientation.Portrait
    val isLandscape: Boolean = orientation == AdaptiveOrientation.Landscape
    val isFolded: Boolean = posture != AdaptivePosture.Normal
    val isCompactWidth: Boolean = windowWidth == AdaptiveWindowWidth.Compact
    val isAtLeastMediumWidth: Boolean = windowWidth >= AdaptiveWindowWidth.Medium
    val isAtLeastExpandedWidth: Boolean = windowWidth >= AdaptiveWindowWidth.Expanded
}

enum class AdaptiveOrientation {
    Portrait,
    Landscape,
}

enum class AdaptiveWindowWidth {
    Compact,
    Medium,
    Expanded,
    Large,
    ExtraLarge,
}

enum class AdaptiveWindowHeight {
    Compact,
    Medium,
    Expanded,
}

enum class AdaptivePosture {
    Normal,
    BookMode,
    TabletopMode,
    Separating,
}
