package com.georgebindragon.android.core.input.keyevent

sealed interface InputKey {
    data class Direction(val direction: DirectionKey) : InputKey
    data object Confirm : InputKey
    data object Back : InputKey
}

