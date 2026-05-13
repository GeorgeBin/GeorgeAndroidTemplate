package com.georgebindragon.android.base.log

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryLogSink(
    private val maxEntries: Int = 300,
) : LogSink {
    private val entriesFlow = MutableStateFlow<List<LogEntry>>(emptyList())

    val entries: StateFlow<List<LogEntry>> = entriesFlow.asStateFlow()

    override fun write(entry: LogEntry) {
        entriesFlow.value = (listOf(entry) + entriesFlow.value).take(maxEntries)
    }
}
