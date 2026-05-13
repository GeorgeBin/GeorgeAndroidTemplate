package com.georgebindragon.android.core.datastore

import kotlinx.coroutines.flow.Flow

interface KeyValueStore {
    suspend fun putString(key: String, value: String?)
    suspend fun getString(key: String, defaultValue: String? = null): String?
    fun observeString(key: String, defaultValue: String? = null): Flow<String?>

    suspend fun putInt(key: String, value: Int)
    suspend fun getInt(key: String, defaultValue: Int = 0): Int
    fun observeInt(key: String, defaultValue: Int = 0): Flow<Int>

    suspend fun putLong(key: String, value: Long)
    suspend fun getLong(key: String, defaultValue: Long = 0L): Long
    fun observeLong(key: String, defaultValue: Long = 0L): Flow<Long>

    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun observeBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean>

    suspend fun putFloat(key: String, value: Float)
    suspend fun getFloat(key: String, defaultValue: Float = 0f): Float
    fun observeFloat(key: String, defaultValue: Float = 0f): Flow<Float>

    suspend fun putStringSet(key: String, value: Set<String>?)
    suspend fun getStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String>
    fun observeStringSet(key: String, defaultValue: Set<String> = emptySet()): Flow<Set<String>>

    suspend fun remove(key: String)
    suspend fun contains(key: String): Boolean
    suspend fun clear()
}
