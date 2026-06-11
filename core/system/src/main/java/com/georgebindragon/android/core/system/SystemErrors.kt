package com.georgebindragon.android.core.system

import com.georgebindragon.android.base.common.AppError

internal fun SystemCapability.unavailableError(): AppError = AppError(
    code = "system_capability_unavailable",
    message = "$name is unavailable. Check capability before using privileged system APIs.",
)

internal fun SystemCapability.missingImplementationError(): AppError = AppError(
    code = "system_capability_missing_implementation",
    message = "$name is available, but no privileged implementation is bound.",
)
