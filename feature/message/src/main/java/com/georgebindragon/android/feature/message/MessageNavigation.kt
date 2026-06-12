package com.georgebindragon.android.feature.message

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.georgebindragon.android.core.navigation.MainRoute

fun NavGraphBuilder.messageScreen() {
    composable(MainRoute.Message) {
        MessageRoute()
    }
}
