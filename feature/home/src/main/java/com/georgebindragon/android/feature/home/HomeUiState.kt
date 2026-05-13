package com.georgebindragon.android.feature.home

import com.georgebindragon.android.core.model.HomeItem

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val items: List<HomeItem>,
    ) : HomeUiState
}
