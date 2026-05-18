package com.georgebindragon.android.core.network.traffic

import kotlin.math.exp
import kotlin.math.ln

internal class ExponentialMovingAverage(
    private val decay: Float,
) {
    private var value = UNDEFINED

    fun add(measurement: Double) {
        value = if (value < 0.0) {
            measurement
        } else {
            exp((1 - decay) * ln(value) + decay * ln(measurement))
        }
    }

    fun average(): Double = value

    fun reset() {
        value = UNDEFINED
    }

    private companion object {
        const val UNDEFINED = -1.0
    }
}
