package com.georgebindragon.android.core.notification

data class AppNotificationChannel(
    val id: String,
    val name: String,
    val description: String? = null,
    val importance: Int,
)

interface NotificationChannelManager {
    fun createChannels(channels: List<AppNotificationChannel>)
    fun deleteChannel(channelId: String)
}
