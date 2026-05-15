package com.georgebindragon.android.core.settings

import com.georgebindragon.android.core.datastore.KeyValueStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DataStoreThemeSettingsRepositoryTest {
    @Test
    fun defaultsToSystemThemeStandardScaleSystemPageOrientationAndExpertModeOff() = runTest {
        val repository = DataStoreThemeSettingsRepository(
            keyValueStore = InMemoryKeyValueStore(),
            scope = backgroundScope,
        )

        assertEquals(ThemeMode.System, repository.themeMode.value)
        assertEquals(AppScale.Standard, repository.appScale.value)
        assertEquals(PageOrientation.System, repository.pageOrientation.value)
        assertEquals(false, repository.expertMode.value)
    }

    @Test
    fun writesThemeModeAppScalePageOrientationAndExpertMode() = runTest {
        val repository = DataStoreThemeSettingsRepository(
            keyValueStore = InMemoryKeyValueStore(),
            scope = backgroundScope,
        )

        repository.setThemeMode(ThemeMode.Dark)
        repository.setAppScale(AppScale.ExtraLarge)
        repository.setPageOrientation(PageOrientation.Rotation90)
        repository.setExpertMode(true)

        assertEquals(ThemeMode.Dark, repository.themeMode.first { it == ThemeMode.Dark })
        assertEquals(AppScale.ExtraLarge, repository.appScale.first { it == AppScale.ExtraLarge })
        assertEquals(
            PageOrientation.Rotation90,
            repository.pageOrientation.first { it == PageOrientation.Rotation90 },
        )
        assertEquals(true, repository.expertMode.first { it })
    }

    @Test
    fun recreatedRepositoryReadsStoredValues() = runTest {
        val store = InMemoryKeyValueStore()
        DataStoreThemeSettingsRepository(
            keyValueStore = store,
            scope = backgroundScope,
        ).setThemeMode(ThemeMode.Light)
        DataStoreThemeSettingsRepository(
            keyValueStore = store,
            scope = backgroundScope,
        ).setAppScale(AppScale.Large)
        DataStoreThemeSettingsRepository(
            keyValueStore = store,
            scope = backgroundScope,
        ).setPageOrientation(PageOrientation.Rotation270)
        DataStoreThemeSettingsRepository(
            keyValueStore = store,
            scope = backgroundScope,
        ).setExpertMode(true)

        val recreatedRepository = DataStoreThemeSettingsRepository(
            keyValueStore = store,
            scope = backgroundScope,
        )

        assertEquals(ThemeMode.Light, recreatedRepository.themeMode.first { it == ThemeMode.Light })
        assertEquals(AppScale.Large, recreatedRepository.appScale.first { it == AppScale.Large })
        assertEquals(
            PageOrientation.Rotation270,
            recreatedRepository.pageOrientation.first { it == PageOrientation.Rotation270 },
        )
        assertEquals(true, recreatedRepository.expertMode.first { it })
    }

    @Test
    fun unknownEnumValuesFallBackToDefaults() = runTest {
        val store = InMemoryKeyValueStore()
        store.putString("theme_mode", "Amoled")
        store.putString("app_scale", "Huge")
        store.putString("page_orientation", "Diagonal")

        val repository = DataStoreThemeSettingsRepository(
            keyValueStore = store,
            scope = backgroundScope,
        )

        assertEquals(ThemeMode.System, repository.themeMode.value)
        assertEquals(AppScale.Standard, repository.appScale.value)
        assertEquals(PageOrientation.System, repository.pageOrientation.value)
    }
}

private class InMemoryKeyValueStore : KeyValueStore {
    private val values = MutableStateFlow<Map<String, Any?>>(emptyMap())

    override suspend fun putString(key: String, value: String?) {
        updateNullable(key, value)
    }

    override suspend fun getString(key: String, defaultValue: String?): String? {
        return observeString(key, defaultValue).first()
    }

    override fun observeString(key: String, defaultValue: String?): Flow<String?> {
        return values.map { it[key] as? String ?: defaultValue }
    }

    override suspend fun putInt(key: String, value: Int) {
        putValue(key, value)
    }

    override suspend fun getInt(key: String, defaultValue: Int): Int {
        return observeInt(key, defaultValue).first()
    }

    override fun observeInt(key: String, defaultValue: Int): Flow<Int> {
        return values.map { it[key] as? Int ?: defaultValue }
    }

    override suspend fun putLong(key: String, value: Long) {
        putValue(key, value)
    }

    override suspend fun getLong(key: String, defaultValue: Long): Long {
        return observeLong(key, defaultValue).first()
    }

    override fun observeLong(key: String, defaultValue: Long): Flow<Long> {
        return values.map { it[key] as? Long ?: defaultValue }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        putValue(key, value)
    }

    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return observeBoolean(key, defaultValue).first()
    }

    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return values.map { it[key] as? Boolean ?: defaultValue }
    }

    override suspend fun putFloat(key: String, value: Float) {
        putValue(key, value)
    }

    override suspend fun getFloat(key: String, defaultValue: Float): Float {
        return observeFloat(key, defaultValue).first()
    }

    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> {
        return values.map { it[key] as? Float ?: defaultValue }
    }

    override suspend fun putStringSet(key: String, value: Set<String>?) {
        updateNullable(key, value)
    }

    override suspend fun getStringSet(key: String, defaultValue: Set<String>): Set<String> {
        return observeStringSet(key, defaultValue).first()
    }

    override fun observeStringSet(key: String, defaultValue: Set<String>): Flow<Set<String>> {
        return values.map { (it[key] as? Set<String>) ?: defaultValue }
    }

    override suspend fun remove(key: String) {
        updateNullable(key, null)
    }

    override suspend fun contains(key: String): Boolean {
        return values.value.containsKey(key)
    }

    override suspend fun clear() {
        values.value = emptyMap()
    }

    private fun putValue(key: String, value: Any) {
        values.value = values.value + (key to value)
    }

    private fun updateNullable(key: String, value: Any?) {
        values.value = values.value.toMutableMap().apply {
            if (value == null) {
                remove(key)
            } else {
                put(key, value)
            }
        }
    }
}
