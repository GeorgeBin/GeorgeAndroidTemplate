package com.georgebindragon.android.core.settings

import com.georgebindragon.android.core.datastore.KeyValueStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DataStoreThemeSettingsRepository(
    private val keyValueStore: KeyValueStore,
    scope: CoroutineScope,
) : ThemeSettingsRepository {
    override val themeMode: StateFlow<ThemeMode> = keyValueStore
        .observeString(KEY_THEME_MODE, ThemeMode.System.name)
        .map { value -> value.toThemeModeOrDefault() }
        .stateIn(scope, SharingStarted.Eagerly, ThemeMode.System)

    override val appScale: StateFlow<AppScale> = keyValueStore
        .observeString(KEY_APP_SCALE, AppScale.Standard.name)
        .map { value -> value.toAppScaleOrDefault() }
        .stateIn(scope, SharingStarted.Eagerly, AppScale.Standard)

    override val pageOrientation: StateFlow<PageOrientation> = keyValueStore
        .observeString(KEY_PAGE_ORIENTATION, PageOrientation.System.name)
        .map { value -> value.toPageOrientationOrDefault() }
        .stateIn(scope, SharingStarted.Eagerly, PageOrientation.System)

    override val expertMode: StateFlow<Boolean> = keyValueStore
        .observeBoolean(KEY_EXPERT_MODE, false)
        .stateIn(scope, SharingStarted.Eagerly, false)

    override suspend fun setThemeMode(mode: ThemeMode) {
        keyValueStore.putString(KEY_THEME_MODE, mode.name)
    }

    override suspend fun setAppScale(scale: AppScale) {
        keyValueStore.putString(KEY_APP_SCALE, scale.name)
    }

    override suspend fun setPageOrientation(orientation: PageOrientation) {
        keyValueStore.putString(KEY_PAGE_ORIENTATION, orientation.name)
    }

    override suspend fun setExpertMode(enabled: Boolean) {
        keyValueStore.putBoolean(KEY_EXPERT_MODE, enabled)
    }

    private fun String?.toThemeModeOrDefault(): ThemeMode {
        return enumValueOrNull<ThemeMode>(this) ?: ThemeMode.System
    }

    private fun String?.toAppScaleOrDefault(): AppScale {
        return enumValueOrNull<AppScale>(this) ?: AppScale.Standard
    }

    private fun String?.toPageOrientationOrDefault(): PageOrientation {
        return enumValueOrNull<PageOrientation>(this) ?: PageOrientation.System
    }

    private inline fun <reified T : Enum<T>> enumValueOrNull(value: String?): T? {
        if (value == null) return null
        return runCatching { enumValueOf<T>(value) }.getOrNull()
    }

    private companion object {
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_APP_SCALE = "app_scale"
        const val KEY_PAGE_ORIENTATION = "page_orientation"
        const val KEY_EXPERT_MODE = "expert_mode"
    }
}
