package dev.spooky.socialmediaapp.core.util

internal fun <T> Result<T>.error(): String = exceptionOrNull()?.message ?: "unexpected error"
