package com.georgebindragon.android.app

import android.content.Context
import com.georgebindragon.android.core.datastore.PreferencesKeyValueStore
import com.georgebindragon.android.core.settings.DataStoreThemeSettingsRepository
import com.georgebindragon.android.core.settings.ThemeSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AppDependencies {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    lateinit var themeSettingsRepository: ThemeSettingsRepository
        private set

    fun init(context: Context) {
        themeSettingsRepository = DataStoreThemeSettingsRepository(
            keyValueStore = PreferencesKeyValueStore(context),
            scope = appScope,
        )
    }
}
