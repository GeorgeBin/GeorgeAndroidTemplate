package com.georgebindragon.android.base.log

enum class LogLevel(
    val priority: Int,
    val symbol: String,
) {
    Verbose(priority = 2, symbol = "V"),
    Debug(priority = 3, symbol = "D"),
    Info(priority = 4, symbol = "I"),
    Warn(priority = 5, symbol = "W"),
    Error(priority = 6, symbol = "E"),
}
