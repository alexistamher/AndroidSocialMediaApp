package dev.spooky.socialmediaapp.core.util

internal fun <T> Result<T>.error(): String {
    return exceptionOrNull()?.message ?: "unexpected error"
}
