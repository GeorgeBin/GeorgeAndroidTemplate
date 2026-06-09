package com.georgebindragon.android.base.common

interface Initializable {
    suspend fun initialize(): AppResult<Unit>
}

interface Destroyable {
    suspend fun destroy(): AppResult<Unit>
}

enum class ComponentState {
    Created,
    Initializing,
    Ready,
    Failed,
    Destroyed,
}
