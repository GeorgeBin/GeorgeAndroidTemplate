package com.georgebindragon.android.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.georgebindragon.android.core.data.HomeRepository
import com.georgebindragon.android.core.data.StaticHomeRepository
import com.georgebindragon.android.core.model.HomeItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    repository: HomeRepository = StaticHomeRepository(),
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = repository.homeItems
        .map<List<HomeItem>, HomeUiState>(HomeUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading,
        )
}
