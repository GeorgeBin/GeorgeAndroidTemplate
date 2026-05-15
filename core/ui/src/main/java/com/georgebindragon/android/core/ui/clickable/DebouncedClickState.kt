package com.georgebindragon.android.core.ui.clickable

import android.os.SystemClock
import androidx.compose.runtime.Stable

const val DefaultClickDebounceIntervalMillis: Long = 500L

@Stable
class DebouncedClickState(
    private val intervalMillis: Long = DefaultClickDebounceIntervalMillis,
    private val nowMillis: () -> Long = SystemClock::uptimeMillis,
) {
    private var lastClickMillis: Long? = null

    fun tryClick(onClick: () -> Unit): Boolean {
        val now = nowMillis()
        val lastClick = lastClickMillis
        if (lastClick != null && now - lastClick < intervalMillis) {
            return false
        }

        lastClickMillis = now
        onClick()
        return true
    }
}
