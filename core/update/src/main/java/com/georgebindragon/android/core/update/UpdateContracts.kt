package com.georgebindragon.android.core.update

import com.georgebindragon.android.base.common.AppResult
import kotlinx.coroutines.flow.StateFlow

interface UpdateChecker {
    val state: StateFlow<UpdateState>

    suspend fun check(currentVersionCode: Long): AppResult<UpdateInfo?>
}

interface UpdateDownloader {
    val state: StateFlow<UpdateState>

    suspend fun download(updateInfo: UpdateInfo): AppResult<String>
}

interface UpdateInstaller {
    val state: StateFlow<UpdateState>

    suspend fun install(filePath: String): AppResult<Unit>
}
