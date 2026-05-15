package com.georgebindragon.android.core.input.focus

import kotlinx.coroutines.flow.StateFlow

interface InteractionModeManager {
    val interactionMode: StateFlow<AppInteractionMode>

    suspend fun setInteractionMode(mode: AppInteractionMode)
}

