package com.georgebindragon.android.core.ui.keyevent

import androidx.compose.ui.input.key.KeyEvent
import com.georgebindragon.android.core.input.keyevent.InputKey
import com.georgebindragon.android.core.input.keyevent.KeyEventMapper

fun KeyEvent.toInputKey(): InputKey? {
    return KeyEventMapper.mapKeyCode(nativeKeyEvent.keyCode)
}

