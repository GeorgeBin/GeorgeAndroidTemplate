package com.georgebindragon.android.base.networktool

interface PingTool {
    suspend fun ping(
        host: String,
        count: Int = 4,
        timeoutMillis: Int = 1_000,
    ): PingResult
}
