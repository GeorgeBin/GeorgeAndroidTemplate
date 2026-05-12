package com.demo.infra.d.template.storage

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object KVManager {

    private val store = MutableStateFlow<Map<String, Any?>>(emptyMap())

    fun init(context: Context) {
        context.applicationContext
    }

    suspend fun putString(key: String, value: String?) {
        store.update { current ->
            current.toMutableMap().apply {
                if (value == null) remove(key) else put(key, value)
            }
        }
    }

    suspend fun getString(key: String, defaultValue: String? = null): String? {
        return observeString(key, defaultValue).first()
    }

    fun observeString(key: String, defaultValue: String? = null): Flow<String?> {
        return store.map { it[key] as? String ?: defaultValue }
    }

    suspend fun putInt(key: String, value: Int) {
        store.update { it + (key to value) }
    }

    suspend fun getInt(key: String, defaultValue: Int = 0): Int {
        return observeInt(key, defaultValue).first()
    }

    fun observeInt(key: String, defaultValue: Int = 0): Flow<Int> {
        return store.map { (it[key] as? Int) ?: defaultValue }
    }

    suspend fun putLong(key: String, value: Long) {
        store.update { it + (key to value) }
    }

    suspend fun getLong(key: String, defaultValue: Long = 0L): Long {
        return observeLong(key, defaultValue).first()
    }

    fun observeLong(key: String, defaultValue: Long = 0L): Flow<Long> {
        return store.map { (it[key] as? Long) ?: defaultValue }
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        store.update { it + (key to value) }
    }

    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return observeBoolean(key, defaultValue).first()
    }

    fun observeBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        return store.map { (it[key] as? Boolean) ?: defaultValue }
    }

    suspend fun putFloat(key: String, value: Float) {
        store.update { it + (key to value) }
    }

    suspend fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return observeFloat(key, defaultValue).first()
    }

    fun observeFloat(key: String, defaultValue: Float = 0f): Flow<Float> {
        return store.map { (it[key] as? Float) ?: defaultValue }
    }

    suspend fun putStringSet(key: String, value: Set<String>?) {
        store.update { current ->
            current.toMutableMap().apply {
                if (value == null) remove(key) else put(key, value)
            }
        }
    }

    suspend fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String> {
        return observeStringSet(key, defaultValue).first()
    }

    fun observeStringSet(key: String, defaultValue: Set<String> = emptySet()): Flow<Set<String>> {
        return store.map { (it[key] as? Set<String>) ?: defaultValue }
    }

    suspend fun remove(key: String) {
        store.update { current ->
            current.toMutableMap().apply {
                remove(key)
            }
        }
    }

    suspend fun contains(key: String): Boolean {
        return store.value.containsKey(key)
    }

    suspend fun clear() {
        store.value = emptyMap()
    }
}
