package com.georgebindragon.android.feature.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.georgebindragon.android.core.appconfig.AuthFeatureConfig
import com.georgebindragon.android.core.auth.AuthRepository
import com.georgebindragon.android.core.navigation.StartupRoute

fun NavGraphBuilder.loginScreen(
    authConfig: AuthFeatureConfig,
    authRepository: AuthRepository,
    onLoginSuccess: () -> Unit,
) {
    composable(StartupRoute.Login) {
        LoginRoute(
            authConfig = authConfig,
            authRepository = authRepository,
            onLoginSuccess = onLoginSuccess,
        )
    }
}
