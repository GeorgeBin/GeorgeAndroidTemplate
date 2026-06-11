package com.georgebindragon.android.base.shell

interface ShellExecutor {
    suspend fun execute(
        command: List<String>,
        timeoutMillis: Long? = null,
        workingDirectory: String? = null,
    ): ShellResult
}
