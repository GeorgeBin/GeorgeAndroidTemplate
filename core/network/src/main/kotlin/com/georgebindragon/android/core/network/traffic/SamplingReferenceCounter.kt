package com.georgebindragon.android.core.network.traffic

import java.util.concurrent.atomic.AtomicInteger

internal class SamplingReferenceCounter {
    private val count = AtomicInteger(0)

    fun increment(): Boolean = count.getAndIncrement() == 0

    fun decrement(): Boolean {
        while (true) {
            val current = count.get()
            if (current <= 0) {
                return false
            }
            if (count.compareAndSet(current, current - 1)) {
                return current == 1
            }
        }
    }

    fun reset() {
        count.set(0)
    }
}
