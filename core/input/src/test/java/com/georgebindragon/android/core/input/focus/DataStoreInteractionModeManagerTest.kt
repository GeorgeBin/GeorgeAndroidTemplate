package com.georgebindragon.android.core.input.focus

import com.georgebindragon.android.core.datastore.KeyValueStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DataStoreInteractionModeManagerTest {
    @Test
    fun defaultsToAuto() = runTest {
        val manager = DataStoreInteractionModeManager(
            keyValueStore = InMemoryKeyValueStore(),
            scope = backgroundScope,
        )

        assertEquals(AppInteractionMode.Auto, manager.interactionMode.value)
    }

    @Test
    fun writesInteractionMode() = runTest {
        val manager = DataStoreInteractionModeManager(
            keyValueStore = InMemoryKeyValueStore(),
            scope = backgroundScope,
        )

        manager.setInteractionMode(AppInteractionMode.Remote)

        assertEquals(
            AppInteractionMode.Remote,
            manager.interactionMode.first { it == AppInteractionMode.Remote },
        )
    }

    @Test
    fun unknownValueFallsBackToAuto() = runTest {
        val store = InMemoryKeyValueStore()
        store.putString("interaction_mode", "KeyboardOnly")

        val manager = DataStoreInteractionModeManager(
            keyValueStore = store,
            scope = backgroundScope,
        )

        assertEquals(AppInteractionMode.Auto, manager.interactionMode.value)
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

    override suspend fun putInt(key: String, value: Int) = putValue(key, value)
    override suspend fun getInt(key: String, defaultValue: Int): Int = observeInt(key, defaultValue).first()
    override fun observeInt(key: String, defaultValue: Int): Flow<Int> = values.map { it[key] as? Int ?: defaultValue }
    override suspend fun putLong(key: String, value: Long) = putValue(key, value)
    override suspend fun getLong(key: String, defaultValue: Long): Long = observeLong(key, defaultValue).first()
    override fun observeLong(key: String, defaultValue: Long): Flow<Long> = values.map { it[key] as? Long ?: defaultValue }
    override suspend fun putBoolean(key: String, value: Boolean) = putValue(key, value)
    override suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean = observeBoolean(key, defaultValue).first()
    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> = values.map { it[key] as? Boolean ?: defaultValue }
    override suspend fun putFloat(key: String, value: Float) = putValue(key, value)
    override suspend fun getFloat(key: String, defaultValue: Float): Float = observeFloat(key, defaultValue).first()
    override fun observeFloat(key: String, defaultValue: Float): Flow<Float> = values.map { it[key] as? Float ?: defaultValue }

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

    override suspend fun contains(key: String): Boolean = values.value.containsKey(key)
    override suspend fun clear() {
        values.value = emptyMap()
    }

    private fun putValue(key: String, value: Any) {
        values.value = values.value + (key to value)
    }

    private fun updateNullable(key: String, value: Any?) {
        values.value = values.value.toMutableMap().apply {
            if (value == null) remove(key) else put(key, value)
        }
    }
}

