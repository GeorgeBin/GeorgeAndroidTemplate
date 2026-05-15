package com.georgebindragon.android.core.input.keyevent

import android.view.KeyEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class KeyEventMapperTest {
    @Test
    fun mapsDirectionKeys() {
        assertEquals(InputKey.Direction(DirectionKey.Up), KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_DPAD_UP))
        assertEquals(InputKey.Direction(DirectionKey.Down), KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_DPAD_DOWN))
        assertEquals(InputKey.Direction(DirectionKey.Left), KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_DPAD_LEFT))
        assertEquals(InputKey.Direction(DirectionKey.Right), KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_DPAD_RIGHT))
    }

    @Test
    fun mapsConfirmKeys() {
        assertEquals(InputKey.Confirm, KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_DPAD_CENTER))
        assertEquals(InputKey.Confirm, KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_ENTER))
        assertEquals(InputKey.Confirm, KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_NUMPAD_ENTER))
        assertEquals(InputKey.Confirm, KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_BUTTON_A))
        assertEquals(InputKey.Confirm, KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_BUTTON_SELECT))
    }

    @Test
    fun mapsBackKeys() {
        assertEquals(InputKey.Back, KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_BACK))
        assertEquals(InputKey.Back, KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_ESCAPE))
    }

    @Test
    fun ignoresUnknownKeys() {
        assertNull(KeyEventMapper.mapKeyCode(KeyEvent.KEYCODE_VOLUME_UP))
    }
}
