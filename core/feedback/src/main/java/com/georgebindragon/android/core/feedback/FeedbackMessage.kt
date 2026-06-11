package com.georgebindragon.android.core.feedback

sealed interface FeedbackMessage {
    val id: String
    val message: String

    data class Toast(
        override val id: String,
        override val message: String,
        val isLong: Boolean = false,
    ) : FeedbackMessage

    data class Snackbar(
        override val id: String,
        override val message: String,
        val actionLabel: String? = null,
    ) : FeedbackMessage

    data class Dialog(
        override val id: String,
        override val message: String,
        val title: String? = null,
        val confirmLabel: String? = null,
    ) : FeedbackMessage
}
