package com.georgebindragon.android.core.feedback

import kotlinx.coroutines.flow.SharedFlow

interface FeedbackManager {
    val messages: SharedFlow<FeedbackMessage>

    suspend fun emit(message: FeedbackMessage)
}
