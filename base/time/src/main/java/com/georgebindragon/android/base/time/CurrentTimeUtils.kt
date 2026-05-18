package com.georgebindragon.android.base.time

import java.time.Clock
import java.time.Instant

object CurrentTimeUtils {
    fun nowMillis(): Long = nowMillis(Clock.systemUTC())

    fun nowMillis(clock: Clock): Long = clock.millis()

    fun nowSeconds(): Long = nowSeconds(Clock.systemUTC())

    fun nowSeconds(clock: Clock): Long = clock.instant().epochSecond

    fun nowInstant(): Instant = nowInstant(Clock.systemUTC())

    fun nowInstant(clock: Clock): Instant = clock.instant()
}
