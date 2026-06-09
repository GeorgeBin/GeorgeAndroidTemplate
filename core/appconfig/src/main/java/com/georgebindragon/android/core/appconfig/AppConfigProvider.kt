package com.georgebindragon.android.core.appconfig

interface AppConfigProvider {
    fun getConfig(): AppConfig
}

class DefaultAppConfigProvider(
    private val config: AppConfig = AppConfig(),
) : AppConfigProvider {
    override fun getConfig(): AppConfig = config
}
