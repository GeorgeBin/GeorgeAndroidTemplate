package com.georgebindragon.android.base.time

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object TimestampUtils {
    fun millisToInstant(timestampMillis: Long): Instant = Instant.ofEpochMilli(timestampMillis)

    fun secondsToInstant(timestampSeconds: Long): Instant = Instant.ofEpochSecond(timestampSeconds)

    fun instantToMillis(instant: Instant): Long = instant.toEpochMilli()

    fun instantToSeconds(instant: Instant): Long = instant.epochSecond

    fun millisToSeconds(timestampMillis: Long): Long = timestampMillis / 1_000L

    fun secondsToMillis(timestampSeconds: Long): Long = timestampSeconds * 1_000L

    fun millisToLocalDateTime(
        timestampMillis: Long,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): LocalDateTime = LocalDateTime.ofInstant(millisToInstant(timestampMillis), zoneId)

    fun secondsToLocalDateTime(
        timestampSeconds: Long,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): LocalDateTime = LocalDateTime.ofInstant(secondsToInstant(timestampSeconds), zoneId)

    fun localDateTimeToMillis(
        localDateTime: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): Long = localDateTime.atZone(zoneId).toInstant().toEpochMilli()

    fun localDateTimeToSeconds(
        localDateTime: LocalDateTime,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): Long = localDateTime.atZone(zoneId).toInstant().epochSecond
}
