package com.georgebindragon.android.app.di

import android.content.Context
import com.georgebindragon.android.core.appconfig.AppConfigProvider
import com.georgebindragon.android.core.appconfig.DefaultAppConfigProvider
import com.georgebindragon.android.core.auth.AuthRepository
import com.georgebindragon.android.core.auth.DataStoreAuthRepository
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
import com.georgebindragon.android.core.permission.PermissionChecker
import com.georgebindragon.android.core.permission.PermissionIntentFactory
import com.georgebindragon.android.core.permission.PermissionRepository
import com.georgebindragon.android.core.privacy.DataStorePrivacyRepository
import com.georgebindragon.android.core.privacy.PrivacyRepository
import com.georgebindragon.android.core.settings.DataStoreThemeSettingsRepository
import com.georgebindragon.android.core.settings.ThemeSettingsRepository
import com.georgebindragon.android.core.startup.DefaultStartupCoordinator
import com.georgebindragon.android.core.startup.StartupCoordinator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    @Provides
    @Singleton
    fun provideKeyValueStore(
        @ApplicationContext context: Context,
    ): KeyValueStore = PreferencesKeyValueStore(context)

    @Provides
    @Singleton
    fun provideAppConfigProvider(): AppConfigProvider = DefaultAppConfigProvider()

    @Provides
    @Singleton
    fun providePrivacyRepository(
        keyValueStore: KeyValueStore,
    ): PrivacyRepository = DataStorePrivacyRepository(keyValueStore)

    @Provides
    @Singleton
    fun providePermissionChecker(
        @ApplicationContext context: Context,
    ): PermissionChecker = AndroidPermissionChecker(context)

    @Provides
    @Singleton
    fun providePermissionRepository(
        keyValueStore: KeyValueStore,
        permissionChecker: PermissionChecker,
    ): PermissionRepository = DataStorePermissionRepository(
        keyValueStore = keyValueStore,
        permissionChecker = permissionChecker,
    )

    @Provides
    @Singleton
    fun providePermissionIntentFactory(
        @ApplicationContext context: Context,
    ): PermissionIntentFactory = AndroidPermissionIntentFactory(context)

    @Provides
    @Singleton
    fun provideAuthRepository(
        keyValueStore: KeyValueStore,
        appScope: CoroutineScope,
    ): AuthRepository = DataStoreAuthRepository(
        keyValueStore = keyValueStore,
        scope = appScope,
    )

    @Provides
    @Singleton
    fun provideStartupCoordinator(
        appConfigProvider: AppConfigProvider,
        privacyRepository: PrivacyRepository,
        permissionRepository: PermissionRepository,
        authRepository: AuthRepository,
    ): StartupCoordinator = DefaultStartupCoordinator(
        appConfigProvider = appConfigProvider,
        privacyRepository = privacyRepository,
        permissionRepository = permissionRepository,
        authRepository = authRepository,
    )

    @Provides
    @Singleton
    fun provideThemeSettingsRepository(
        keyValueStore: KeyValueStore,
        appScope: CoroutineScope,
    ): ThemeSettingsRepository = DataStoreThemeSettingsRepository(
        keyValueStore = keyValueStore,
        scope = appScope,
    )

    @Provides
    @Singleton
    fun provideInteractionModeManager(
        keyValueStore: KeyValueStore,
        appScope: CoroutineScope,
    ): InteractionModeManager = DataStoreInteractionModeManager(
        keyValueStore = keyValueStore,
        scope = appScope,
    )

    @Provides
    @Singleton
    fun provideSetInteractionModeUseCase(
        interactionModeManager: InteractionModeManager,
    ): SetInteractionModeUseCase = SetInteractionModeUseCase(interactionModeManager)

    @Provides
    @Singleton
    fun provideLanguageManager(): LanguageManager = AppCompatLanguageManager()

    @Provides
    @Singleton
    fun provideSetAppLanguageUseCase(
        languageManager: LanguageManager,
    ): SetAppLanguageUseCase = SetAppLanguageUseCase(languageManager)
}
