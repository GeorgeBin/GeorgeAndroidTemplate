package com.georgebindragon.kotlin

import java.io.Closeable

inline fun <T> T.runIf(condition: Boolean, block: T.() -> T): T = if (condition) block() else this

inline fun <T> List<T>.runIfNotEmpty(block: List<T>.() -> Unit) {
    if (isNotEmpty()) {
        block()
    }
}

fun Closeable.closeQuietly() {
    try {
        close()
    } catch (ignore: Throwable) {
    }
}