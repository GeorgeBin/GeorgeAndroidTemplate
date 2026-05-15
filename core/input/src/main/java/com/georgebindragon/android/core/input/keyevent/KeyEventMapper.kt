package com.georgebindragon.android.core.input.keyevent

import android.view.KeyEvent

object KeyEventMapper {
    fun mapKeyCode(keyCode: Int): InputKey? = when (keyCode) {
        KeyEvent.KEYCODE_DPAD_UP -> InputKey.Direction(DirectionKey.Up)
        KeyEvent.KEYCODE_DPAD_DOWN -> InputKey.Direction(DirectionKey.Down)
        KeyEvent.KEYCODE_DPAD_LEFT -> InputKey.Direction(DirectionKey.Left)
        KeyEvent.KEYCODE_DPAD_RIGHT -> InputKey.Direction(DirectionKey.Right)
        KeyEvent.KEYCODE_DPAD_CENTER,
        KeyEvent.KEYCODE_ENTER,
        KeyEvent.KEYCODE_NUMPAD_ENTER,
        KeyEvent.KEYCODE_BUTTON_A,
        KeyEvent.KEYCODE_BUTTON_SELECT,
        -> InputKey.Confirm
        KeyEvent.KEYCODE_BACK,
        KeyEvent.KEYCODE_ESCAPE,
        -> InputKey.Back
        else -> null
    }
}
