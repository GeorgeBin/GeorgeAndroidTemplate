package com.georgebindragon.android.base.log

data class LogConfig(
    val minLevel: LogLevel = LogLevel.Verbose,
    val sinks: List<LogSink>,
    val queueCapacity: Int = 1_024,
    val overflowStrategy: LogOverflowStrategy = LogOverflowStrategy.DropOldest,
) {
    init {
        require(queueCapacity > 0) { "queueCapacity must be greater than 0." }
    }
}

enum class LogOverflowStrategy {
    DropOldest,
    DropLatest,
}
