package com.demo.infra.d.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.demo.infra.d.template.core.designsystem.theme.TemplateTheme
import com.jakewharton.processphoenix.ProcessPhoenix

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val packageName = applicationContext.packageName
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown"
        setContent {
            TemplateTheme {
                TemplateApp(
                    appName = stringResource(R.string.app_name),
                    packageName = packageName,
                    versionName = versionName,
                    onExitClick = { finishAffinity() },
                    onRestartClick = { ProcessPhoenix.triggerRebirth(this) },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
