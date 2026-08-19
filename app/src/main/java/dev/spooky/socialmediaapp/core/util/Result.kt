package dev.spooky.socialmediaapp.core.util

internal fun <T> Result<T>.error(): String = exceptionOrNull()?.message ?: "unexpected error"

internal fun <T> Result.Companion.failed(message: String): Result<T> = Result.failure(Throwable(message))
