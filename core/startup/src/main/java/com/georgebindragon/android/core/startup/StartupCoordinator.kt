package com.georgebindragon.android.core.startup

interface StartupCoordinator {
    suspend fun resolveDestination(): StartupDestination
}
