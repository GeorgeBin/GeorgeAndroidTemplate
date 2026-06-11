package com.georgebindragon.android.feature.privacy

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.georgebindragon.android.core.appconfig.PrivacyFeatureConfig
import com.georgebindragon.android.core.navigation.StartupRoute
import com.georgebindragon.android.core.privacy.PrivacyRepository

fun NavGraphBuilder.privacyScreen(
    privacyConfig: PrivacyFeatureConfig,
    privacyRepository: PrivacyRepository,
    onAccepted: () -> Unit,
    onRejected: () -> Unit,
) {
    composable(StartupRoute.Privacy) {
        PrivacyRoute(
            privacyConfig = privacyConfig,
            privacyRepository = privacyRepository,
            onAccepted = onAccepted,
            onRejected = onRejected,
        )
    }
}
