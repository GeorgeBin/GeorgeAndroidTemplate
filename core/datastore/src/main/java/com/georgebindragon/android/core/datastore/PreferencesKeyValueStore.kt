package com.georgebindragon.android.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferencesKeyValueStore(
    private val dataStore: DataStore<Preferences>,
) : KeyValueStore {
    constructor(
        context: Context,
        name: String = DEFAULT_NAME,
    ) : this(
        dataStore = PreferenceDataStoreFactory.create(
            produceFile = { context.applicationContext.preferencesDataStoreFile(name) },
        ),
    )

    override suspend fun putString(key: String, value: String?) {
        val preferenceKey = PreferenceInterop.stringKey(key)
        dataStore.editPreferences { preferences ->
            if (value == null) {
                preferences.remove(preferenceKey)
            } else {
                preferences[preferenceKey] = value
            }
        }
    }

    override suspend fun getString(key: String, defaultValue: String?): String? {
        return observeString(key, defaultValue).first()
    }

    override fun observeString(key: String, defaultValue: String?): Flow<String?> {
        val preferenceKey = PreferenceInterop.stringKey(key)
        return preferencesFlow.map { preferences -> preferences[preferenceKey] ?: defaultValue }
    }

    override suspend fun putInt(key: String, value: Int) {
        val preferenceKey = PreferenceInterop.intKey(key)
        dataStore.editPreferences { preferences -> preferences[preferenceKey] = value }
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return observeInt(key, defaultValue).first()
    }

    override fun observeInt(key: String, defaultValue: Int): Flow<Int> {
        val preferenceKey = PreferenceInterop.intKey(key)
        return preferencesFlow.map { preferences -> preferences[preferenceKey] ?: defaultValue }
    }

    override suspend fun putLong(key: String, value: Long) {
        val preferenceKey = PreferenceInterop.longKey(key)
        dataStore.editPreferences { preferences -> preferences[preferenceKey] = value }
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        return observeLong(key, defaultValue).first()
    }

    override fun observeLong(key: String, defaultValue: Long): Flow<Long> {
        val preferenceKey = PreferenceInterop.longKey(key)
        return preferencesFlow.map { preferences -> preferences[preferenceKey] ?: defaultValue }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferenceKey = PreferenceInterop.booleanKey(key)
        dataStore.editPreferences { preferences -> preferences[preferenceKey] = value }
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return observeBoolean(key, defaultValue).first()
    }

    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        val preferenceKey = PreferenceInterop.booleanKey(key)
        return preferencesFlow.map { preferences -> preferences[preferenceKey] ?: defaultValue }
    }

    override suspend fun putFloat(key: String, value: Float) {
        val preferenceKey = PreferenceInterop.floatKey(key)
        dataStore.editPreferences { preferences -> preferences[preferenceKey] = value }
    }

    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        return observeFloat(key, defaultValue).first()
    }

    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> {
        val preferenceKey = PreferenceInterop.floatKey(key)
        return preferencesFlow.map { preferences -> preferences[preferenceKey] ?: defaultValue }
    }

    override suspend fun putStringSet(key: String, value: Set<String>?) {
        val preferenceKey = PreferenceInterop.stringSetKey(key)
        dataStore.editPreferences { preferences ->
            if (value == null) {
                preferences.remove(preferenceKey)
            } else {
                preferences[preferenceKey] = value
            }
        }
    }

    override suspend fun getStringSet(key: String, defaultValue: Set<String>): Set<String> {
        return observeStringSet(key, defaultValue).first()
    }

    override fun observeStringSet(key: String, defaultValue: Set<String>): Flow<Set<String>> {
        val preferenceKey = PreferenceInterop.stringSetKey(key)
        return preferencesFlow.map { preferences -> preferences[preferenceKey] ?: defaultValue }
    }

    override suspend fun remove(key: String) {
        dataStore.editPreferences { preferences ->
            preferences.remove(PreferenceInterop.stringKey(key))
            preferences.remove(PreferenceInterop.intKey(key))
            preferences.remove(PreferenceInterop.longKey(key))
            preferences.remove(PreferenceInterop.booleanKey(key))
            preferences.remove(PreferenceInterop.floatKey(key))
            preferences.remove(PreferenceInterop.stringSetKey(key))
        }
    }

    override suspend fun contains(key: String): Boolean {
        val preferences = preferencesFlow.first()
        return preferences.contains(PreferenceInterop.stringKey(key)) ||
            preferences.contains(PreferenceInterop.intKey(key)) ||
            preferences.contains(PreferenceInterop.longKey(key)) ||
            preferences.contains(PreferenceInterop.booleanKey(key)) ||
            preferences.contains(PreferenceInterop.floatKey(key)) ||
            preferences.contains(PreferenceInterop.stringSetKey(key))
    }

    override suspend fun clear() {
        dataStore.editPreferences { preferences -> preferences.clear() }
    }

    private suspend fun DataStore<Preferences>.editPreferences(
        transform: (androidx.datastore.preferences.core.MutablePreferences) -> Unit,
    ) {
        updateData { preferences ->
            preferences.toMutablePreferences().apply(transform).toPreferences()
        }
    }

    private val preferencesFlow: Flow<Preferences> = dataStore.data.catch { throwable ->
        if (throwable is IOException) {
            emit(PreferenceInterop.emptyPreferences())
        } else {
            throw throwable
        }
    }

    companion object {
        const val DEFAULT_NAME = "app_preferences"
    }
}
