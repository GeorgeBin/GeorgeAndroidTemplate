package com.georgebindragon.android.base.time

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeFormatUtils {
    const val DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss"

    fun formatMillis(
        timestampMillis: Long,
        pattern: String = DEFAULT_PATTERN,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): String = format(
        instant = Instant.ofEpochMilli(timestampMillis),
        pattern = pattern,
        zoneId = zoneId,
    )

    fun formatSeconds(
        timestampSeconds: Long,
        pattern: String = DEFAULT_PATTERN,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): String = format(
        instant = Instant.ofEpochSecond(timestampSeconds),
        pattern = pattern,
        zoneId = zoneId,
    )

    fun format(
        instant: Instant,
        pattern: String = DEFAULT_PATTERN,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): String = format(
        instant = instant,
        formatter = DateTimeFormatter.ofPattern(pattern),
        zoneId = zoneId,
    )

    fun format(
        instant: Instant,
        formatter: DateTimeFormatter,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): String = formatter.withZone(zoneId).format(instant)
}
