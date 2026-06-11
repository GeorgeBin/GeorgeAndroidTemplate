package com.georgebindragon.android.base.shell

data class ShellResult(
    val command: List<String>,
    val exitCode: Int?,
    val standardOutput: String,
    val standardError: String,
    val durationMillis: Long,
    val timedOut: Boolean = false,
) {
    val isSuccess: Boolean = exitCode == 0 && !timedOut
}
