package com.georgebindragon.android.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(ExperimentalCoroutinesApi::class)
class PreferencesKeyValueStoreTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun storesAndReadsSupportedTypes() = runTest {
        val store = newStore("supported-types.preferences_pb")

        store.putString("string", "value")
        store.putInt("int", 7)
        store.putLong("long", 9L)
        store.putBoolean("boolean", true)
        store.putFloat("float", 1.5f)
        store.putStringSet("string_set", setOf("a", "b"))

        assertEquals("value", store.getString("string"))
        assertEquals(7, store.getInt("int"))
        assertEquals(9L, store.getLong("long"))
        assertTrue(store.getBoolean("boolean"))
        assertEquals(1.5f, store.getFloat("float"), 0.0f)
        assertEquals(setOf("a", "b"), store.getStringSet("string_set"))
    }

    @Test
    fun observesValuesAndDefaults() = runTest {
        val store = newStore("observe.preferences_pb")

        assertEquals("default", store.observeString("missing", "default").first())
        store.putString("missing", "stored")

        assertEquals("stored", store.observeString("missing", "default").first())
    }

    @Test
    fun nullStringRemovesKey() = runTest {
        val store = newStore("null-string.preferences_pb")

        store.putString("string", "value")
        assertTrue(store.contains("string"))

        store.putString("string", null)

        assertFalse(store.contains("string"))
        assertNull(store.getString("string"))
    }

    @Test
    fun nullStringSetRemovesKey() = runTest {
        val store = newStore("null-string-set.preferences_pb")

        store.putStringSet("string_set", setOf("a"))
        assertTrue(store.contains("string_set"))

        store.putStringSet("string_set", null)

        assertFalse(store.contains("string_set"))
        assertEquals(emptySet<String>(), store.getStringSet("string_set"))
    }

    @Test
    fun removeAndClearDeleteStoredValues() = runTest {
        val store = newStore("remove-clear.preferences_pb")

        store.putInt("int", 1)
        store.putBoolean("boolean", true)
        assertTrue(store.contains("int"))
        assertTrue(store.contains("boolean"))

        store.remove("int")
        assertFalse(store.contains("int"))
        assertTrue(store.contains("boolean"))

        store.clear()
        assertFalse(store.contains("boolean"))
    }

    private fun TestScope.newStore(fileName: String): PreferencesKeyValueStore {
        val file = File(temporaryFolder.root, fileName)
        return PreferencesKeyValueStore(
            PreferenceDataStoreFactory.create(
                scope = backgroundScope,
                produceFile = { file },
            ),
        )
    }
}
