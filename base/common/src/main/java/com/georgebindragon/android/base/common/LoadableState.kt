package com.georgebindragon.android.base.common

sealed interface LoadableState<out T> {
    data object Idle : LoadableState<Nothing>
    data object Loading : LoadableState<Nothing>
    data class Loaded<T>(val value: T) : LoadableState<T>
    data class Failed(val error: AppError) : LoadableState<Nothing>
}
