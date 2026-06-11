package com.georgebindragon.android.core.permission

import com.georgebindragon.android.base.common.UiText
import com.georgebindragon.android.core.datastore.KeyValueStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DataStorePermissionRepositoryTest {
    @Test
    fun firstLaunchShowsPermissionGateWhenOverviewIsEnabled() = runTest {
        val repository = DataStorePermissionRepository(
            keyValueStore = InMemoryKeyValueStore(),
            permissionChecker = FakePermissionChecker(granted = true),
        )

        assertTrue(
            repository.shouldShowPermissionGate(
                enabled = true,
                declarations = listOf(notificationDeclaration(required = false)),
                showOverviewOnFirstLaunch = true,
                overviewVersion = 1,
            ),
        )
    }

    @Test
    fun seenOverviewAndGrantedPermissionsSkipGate() = runTest {
        val repository = DataStorePermissionRepository(
            keyValueStore = InMemoryKeyValueStore(),
            permissionChecker = FakePermissionChecker(granted = true),
        )

        repository.markOverviewSeen(overviewVersion = 1)

        assertFalse(
            repository.shouldShowPermissionGate(
                enabled = true,
                declarations = listOf(notificationDeclaration(required = false)),
                showOverviewOnFirstLaunch = true,
                overviewVersion = 1,
            ),
        )
    }

    @Test
    fun missingRequiredPermissionShowsGate() = runTest {
        val repository = DataStorePermissionRepository(
            keyValueStore = InMemoryKeyValueStore(),
            permissionChecker = FakePermissionChecker(granted = false),
        )

        repository.markOverviewSeen(overviewVersion = 1)

        assertTrue(
            repository.shouldShowPermissionGate(
                enabled = true,
                declarations = listOf(notificationDeclaration(required = true)),
                showOverviewOnFirstLaunch = true,
                overviewVersion = 1,
            ),
        )
    }

    @Test
    fun skippedOptionalPermissionDoesNotShowGateAgain() = runTest {
        val repository = DataStorePermissionRepository(
            keyValueStore = InMemoryKeyValueStore(),
            permissionChecker = FakePermissionChecker(granted = false),
        )
        val declaration = notificationDeclaration(required = false)

        repository.markOverviewSeen(overviewVersion = 1)
        repository.skipOptionalPermissions(listOf(declaration.permission))

        assertFalse(
            repository.shouldShowPermissionGate(
                enabled = true,
                declarations = listOf(declaration),
                showOverviewOnFirstLaunch = true,
                overviewVersion = 1,
            ),
        )
    }

    private fun notificationDeclaration(required: Boolean): AppPermissionDeclaration {
        return AppPermissionDeclaration(
            permission = AppPermission.Notification,
            title = UiText.Plain("Notification"),
            description = UiText.Plain("Show notifications"),
            required = required,
            requestTiming = PermissionRequestTiming.OnStartup,
        )
    }
}

private class FakePermissionChecker(
    private val granted: Boolean,
) : PermissionChecker {
    override fun isGranted(permission: AppPermission): Boolean = granted
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
    override fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return values.map { it[key] as? Boolean ?: defaultValue }
    }

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
