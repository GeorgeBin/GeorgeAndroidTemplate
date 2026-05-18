package com.georgebindragon.android.base.time

import java.time.Clock
import java.time.Duration
import java.time.Instant

object TimeDiffUtils {
    fun betweenMillis(
        startMillis: Long,
        endMillis: Long,
    ): Duration = Duration.ofMillis(endMillis - startMillis)

    fun betweenInstants(
        start: Instant,
        end: Instant,
    ): Duration = Duration.between(start, end)

    fun elapsedSinceMillis(
        startMillis: Long,
        clock: Clock = Clock.systemUTC(),
    ): Duration = betweenMillis(startMillis, clock.millis())

    fun elapsedSinceInstant(
        start: Instant,
        clock: Clock = Clock.systemUTC(),
    ): Duration = betweenInstants(start, clock.instant())
}
