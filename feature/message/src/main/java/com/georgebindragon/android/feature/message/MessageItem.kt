package com.georgebindragon.android.feature.message

data class MessageItem(
    val id: String,
    val sender: String,
    val title: String,
    val summary: String,
    val time: String,
    val read: Boolean = false,
    val pinned: Boolean = false,
)
