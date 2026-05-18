package com.georgebindragon.android.base.crash

import java.io.File

data class CrashLogResult(
    val file: File?,
    val throwable: Throwable,
    val threadName: String,
    val writeError: Throwable?,
) {
    val saved: Boolean = file != null && writeError == null
}
