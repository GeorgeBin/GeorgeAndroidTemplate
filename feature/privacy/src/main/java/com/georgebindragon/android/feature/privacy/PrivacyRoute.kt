package com.georgebindragon.android.feature.privacy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.georgebindragon.android.base.common.AppResult
import com.georgebindragon.android.core.appconfig.PrivacyFeatureConfig
import com.georgebindragon.android.core.privacy.PrivacyRepository
import kotlinx.coroutines.launch

@Composable
fun PrivacyRoute(
    privacyConfig: PrivacyFeatureConfig,
    privacyRepository: PrivacyRepository,
    onAccepted: () -> Unit,
    onRejected: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PrivacyViewModel = remember(privacyRepository, privacyConfig) {
        PrivacyViewModel(
            privacyRepository = privacyRepository,
            privacyConfig = privacyConfig,
        )
    },
) {
    val coroutineScope = rememberCoroutineScope()
    var uiState by remember(viewModel) { mutableStateOf(viewModel.initialState) }

    PrivacyScreen(
        uiState = uiState,
        onAcceptClick = {
            if (uiState.accepting) return@PrivacyScreen
            uiState = uiState.copy(accepting = true, errorMessage = null)
            coroutineScope.launch {
                when (val result = viewModel.acceptPrivacy()) {
                    is AppResult.Success -> onAccepted()
                    is AppResult.Failure -> {
                        uiState = uiState.copy(
                            accepting = false,
                            errorMessage = result.error.message,
                        )
                    }
                }
            }
        },
        onRejectClick = onRejected,
        modifier = modifier,
    )
}
