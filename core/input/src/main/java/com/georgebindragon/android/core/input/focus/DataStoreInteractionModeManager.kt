package com.georgebindragon.android.core.input.focus

import com.georgebindragon.android.core.datastore.KeyValueStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DataStoreInteractionModeManager(
    private val keyValueStore: KeyValueStore,
    scope: CoroutineScope,
) : InteractionModeManager {
    override val interactionMode: StateFlow<AppInteractionMode> = keyValueStore
        .observeString(KEY_INTERACTION_MODE, AppInteractionMode.Auto.name)
        .map { value -> value.toInteractionModeOrDefault() }
        .stateIn(scope, SharingStarted.Eagerly, AppInteractionMode.Auto)

    override suspend fun setInteractionMode(mode: AppInteractionMode) {
        keyValueStore.putString(KEY_INTERACTION_MODE, mode.name)
    }

    private fun String?.toInteractionModeOrDefault(): AppInteractionMode {
        if (this == null) return AppInteractionMode.Auto
        return runCatching { enumValueOf<AppInteractionMode>(this) }
            .getOrDefault(AppInteractionMode.Auto)
    }

    private companion object {
        const val KEY_INTERACTION_MODE = "interaction_mode"
    }
}

