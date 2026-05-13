package com.demo.infra.d.template.feature.home

import com.demo.infra.d.template.core.model.HomeItem

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val items: List<HomeItem>,
    ) : HomeUiState
}
