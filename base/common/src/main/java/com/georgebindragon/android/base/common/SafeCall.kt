package com.georgebindragon.android.base.common

suspend inline fun <T> safeCall(
    errorCode: String,
    crossinline block: suspend () -> T,
): AppResult<T> {
    return try {
        AppResult.Success(block())
    } catch (throwable: Throwable) {
        AppResult.Failure(
            AppError(
                code = errorCode,
                message = throwable.message ?: throwable::class.java.simpleName,
                cause = throwable,
            ),
        )
    }
}
