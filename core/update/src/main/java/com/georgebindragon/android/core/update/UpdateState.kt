package com.georgebindragon.android.core.update

data class UpdateInfo(
    val versionCode: Long,
    val versionName: String,
    val changelog: String = "",
    val downloadUrl: String = "",
    val forceUpdate: Boolean = false,
)

sealed interface UpdateState {
    data object Idle : UpdateState
    data object Checking : UpdateState
    data object NoUpdate : UpdateState
    data class Available(val updateInfo: UpdateInfo) : UpdateState
    data class Downloading(val updateInfo: UpdateInfo, val progress: Float) : UpdateState
    data class Downloaded(val updateInfo: UpdateInfo, val filePath: String) : UpdateState
    data class Installing(val filePath: String) : UpdateState
    data class Failed(val reason: String) : UpdateState
}
