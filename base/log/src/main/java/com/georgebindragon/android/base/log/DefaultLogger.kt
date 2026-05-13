package com.georgebindragon.android.base.log

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

class DefaultLogger(
    private val config: LogConfig,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) : Logger {
    private val ownsScope = true
    private val closed = AtomicBoolean(false)
    private val workerScope = scope
    private val events = Channel<LogEvent>(
        capacity = config.queueCapacity,
        onBufferOverflow = when (config.overflowStrategy) {
            LogOverflowStrategy.DropOldest -> BufferOverflow.DROP_OLDEST
            LogOverflowStrategy.DropLatest -> BufferOverflow.DROP_LATEST
        },
    )

    init {
        workerScope.launch {
            for (event in events) {
                when (event) {
                    is LogEvent.Entry -> writeToSinks(event.entry)
                    is LogEvent.Flush -> {
                        flushSinks()
                        event.result.complete(Unit)
                    }
                    is LogEvent.Close -> {
                        flushSinks()
                        closeSinks()
                        event.result.complete(Unit)
                        break
                    }
                }
            }
        }
    }

    override fun log(entry: LogEntry) {
        if (closed.get()) return
        if (entry.level.priority < config.minLevel.priority) return
        events.trySend(LogEvent.Entry(entry))
    }

    override fun flush() {
        if (closed.get()) return
        val result = CompletableDeferred<Unit>()
        if (events.trySend(LogEvent.Flush(result)).isFailure) return
        runBlocking { result.await() }
    }

    override fun close() {
        if (closed.getAndSet(true)) return
        val result = CompletableDeferred<Unit>()
        if (events.trySend(LogEvent.Close(result)).isSuccess) {
            runBlocking { result.await() }
        }
        events.close()
        if (ownsScope) workerScope.cancel()
    }

    private fun writeToSinks(entry: LogEntry) {
        config.sinks.forEach { sink ->
            runCatching { sink.write(entry) }
                .onFailure { LogcatLogSink.writeFallback("DefaultLogger", it) }
        }
    }

    private fun flushSinks() {
        config.sinks.forEach { sink ->
            runCatching { sink.flush() }
                .onFailure { LogcatLogSink.writeFallback("DefaultLogger", it) }
        }
    }

    private fun closeSinks() {
        config.sinks.forEach { sink ->
            if (sink is Closeable) {
                runCatching { sink.close() }
                    .onFailure { LogcatLogSink.writeFallback("DefaultLogger", it) }
            }
        }
    }

    private sealed interface LogEvent {
        data class Entry(val entry: LogEntry) : LogEvent
        data class Flush(val result: CompletableDeferred<Unit>) : LogEvent
        data class Close(val result: CompletableDeferred<Unit>) : LogEvent
    }
}
