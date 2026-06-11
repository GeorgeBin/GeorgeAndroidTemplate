package com.georgebindragon.android.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.georgebindragon.android.core.appconfig.AuthFeatureConfig
import com.georgebindragon.android.core.auth.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    authConfig: AuthFeatureConfig,
    authRepository: AuthRepository,
    onLoginSuccess: () -> Unit,
) {
    val viewModel = remember(authRepository) {
        LoginViewModel(authRepository)
    }
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    LoginScreen(
        uiState = uiState,
        allowGuestMode = authConfig.allowGuestMode,
        onAccountChange = viewModel::onAccountChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = {
            scope.launch {
                if (viewModel.login()) {
                    onLoginSuccess()
                }
            }
        },
        onGuestClick = {
            scope.launch {
                if (viewModel.continueAsGuest()) {
                    onLoginSuccess()
                }
            }
        },
    )
}
