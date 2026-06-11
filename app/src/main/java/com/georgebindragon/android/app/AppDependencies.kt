package com.georgebindragon.android.app

import android.content.Context
import com.georgebindragon.android.core.appconfig.AppConfigProvider
import com.georgebindragon.android.core.appconfig.DefaultAppConfigProvider
import com.georgebindragon.android.core.datastore.KeyValueStore
import com.georgebindragon.android.core.datastore.PreferencesKeyValueStore
import com.georgebindragon.android.core.input.focus.DataStoreInteractionModeManager
import com.georgebindragon.android.core.input.focus.InteractionModeManager
import com.georgebindragon.android.core.input.focus.SetInteractionModeUseCase
import com.georgebindragon.android.core.locale.AppCompatLanguageManager
import com.georgebindragon.android.core.locale.LanguageManager
import com.georgebindragon.android.core.locale.SetAppLanguageUseCase
import com.georgebindragon.android.core.permission.AndroidPermissionChecker
import com.georgebindragon.android.core.permission.AndroidPermissionIntentFactory
import com.georgebindragon.android.core.permission.DataStorePermissionRepository
import com.georgebindragon.android.core.permission.PermissionIntentFactory
import com.georgebindragon.android.core.permission.PermissionRepository
import com.georgebindragon.android.core.privacy.DataStorePrivacyRepository
import com.georgebindragon.android.core.privacy.PrivacyRepository
import com.georgebindragon.android.core.settings.DataStoreThemeSettingsRepository
import com.georgebindragon.android.core.settings.ThemeSettingsRepository
import com.georgebindragon.android.core.startup.DefaultStartupCoordinator
import com.georgebindragon.android.core.startup.StartupCoordinator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AppDependencies {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private lateinit var keyValueStore: KeyValueStore

    lateinit var themeSettingsRepository: ThemeSettingsRepository
        private set

    lateinit var interactionModeManager: InteractionModeManager
        private set

    lateinit var setInteractionModeUseCase: SetInteractionModeUseCase
        private set

    lateinit var languageManager: LanguageManager
        private set

    lateinit var setAppLanguageUseCase: SetAppLanguageUseCase
        private set

    lateinit var appConfigProvider: AppConfigProvider
        private set

    lateinit var privacyRepository: PrivacyRepository
        private set

    lateinit var permissionRepository: PermissionRepository
        private set

    lateinit var permissionIntentFactory: PermissionIntentFactory
        private set

    lateinit var startupCoordinator: StartupCoordinator
        private set

    fun init(context: Context) {
        keyValueStore = PreferencesKeyValueStore(context)
        appConfigProvider = DefaultAppConfigProvider()
        privacyRepository = DataStorePrivacyRepository(keyValueStore)
        permissionRepository = DataStorePermissionRepository(
            keyValueStore = keyValueStore,
            permissionChecker = AndroidPermissionChecker(context.applicationContext),
        )
        permissionIntentFactory = AndroidPermissionIntentFactory(context.applicationContext)
        startupCoordinator = DefaultStartupCoordinator(
            appConfigProvider = appConfigProvider,
            privacyRepository = privacyRepository,
            permissionRepository = permissionRepository,
        )
        themeSettingsRepository = DataStoreThemeSettingsRepository(
            keyValueStore = keyValueStore,
            scope = appScope,
        )
        interactionModeManager = DataStoreInteractionModeManager(
            keyValueStore = keyValueStore,
            scope = appScope,
        )
        setInteractionModeUseCase = SetInteractionModeUseCase(interactionModeManager)
        languageManager = AppCompatLanguageManager()
        setAppLanguageUseCase = SetAppLanguageUseCase(languageManager)
    }
}
