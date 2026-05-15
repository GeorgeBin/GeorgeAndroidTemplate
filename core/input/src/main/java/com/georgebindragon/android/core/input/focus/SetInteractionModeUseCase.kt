package com.georgebindragon.android.core.input.focus

class SetInteractionModeUseCase(
    private val interactionModeManager: InteractionModeManager,
) {
    suspend operator fun invoke(mode: AppInteractionMode) {
        interactionModeManager.setInteractionMode(mode)
    }
}

