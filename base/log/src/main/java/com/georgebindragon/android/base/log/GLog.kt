package com.georgebindragon.android.base.log

object GLog {
    @Volatile
    private var logger: Logger = DefaultLogger(
        LogConfig(sinks = listOf(LogcatLogSink)),
    )

    fun install(logger: Logger) {
        this.logger.close()
        this.logger = logger
    }

    fun reset() {
        install(
            DefaultLogger(
                LogConfig(sinks = listOf(LogcatLogSink)),
            ),
        )
    }

    fun v(tag: String, message: String, throwable: Throwable? = null) =
        logger.log(LogEntry(LogLevel.Verbose, tag, message, throwable.stackTraceTextOrNull()))

    fun d(tag: String, message: String, throwable: Throwable? = null) =
        logger.log(LogEntry(LogLevel.Debug, tag, message, throwable.stackTraceTextOrNull()))

    fun i(tag: String, message: String, throwable: Throwable? = null) =
        logger.log(LogEntry(LogLevel.Info, tag, message, throwable.stackTraceTextOrNull()))

    fun w(tag: String, message: String, throwable: Throwable? = null) =
        logger.log(LogEntry(LogLevel.Warn, tag, message, throwable.stackTraceTextOrNull()))

    fun e(tag: String, message: String, throwable: Throwable? = null) =
        logger.log(LogEntry(LogLevel.Error, tag, message, throwable.stackTraceTextOrNull()))

    fun flush() = logger.flush()

    fun close() = logger.close()
}
