package com.georgebindragon.android.app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.georgebindragon.android.core.startup.StartupCoordinator
import com.georgebindragon.android.core.startup.StartupDestination

@Composable
fun StartupRoute(
    startupCoordinator: StartupCoordinator,
    onDestinationResolved: (StartupDestination) -> Unit,
) {
    LaunchedEffect(startupCoordinator) {
        onDestinationResolved(startupCoordinator.resolveDestination())
    }

    Text(text = "启动流程")
}
