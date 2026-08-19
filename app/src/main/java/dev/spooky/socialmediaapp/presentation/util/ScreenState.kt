package dev.spooky.socialmediaapp.presentation.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal interface ScreenState<out T> {
    data object Idle : ScreenState<Nothing>

    data class Loading<T>(
        val data: T? = null,
    ) : ScreenState<T>

    data class Success<T>(
        val data: T,
    ) : ScreenState<T>

    data class Error(
        val message: String,
    ) : ScreenState<Nothing>
}

@OptIn(ExperimentalContracts::class)
internal inline fun <reified T> ScreenState<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is ScreenState.Success)
    }
    return this is ScreenState.Success
}

@OptIn(ExperimentalContracts::class)
internal inline fun <reified T> ScreenState<T>.isLoading(): Boolean {
    contract {
        returns(true) implies (this@isLoading is ScreenState.Loading)
    }
    return this is ScreenState.Loading
}

@OptIn(ExperimentalContracts::class)
internal inline fun <reified T> ScreenState<T>.isIle(): Boolean {
    contract {
        returns(true) implies (this@isIle is ScreenState.Idle)
    }
    return this is ScreenState.Idle
}

@OptIn(ExperimentalContracts::class)
internal inline fun <reified T> ScreenState<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is ScreenState.Error)
    }
    return this is ScreenState.Error
}

@OptIn(ExperimentalContracts::class)
internal inline fun <reified T> ScreenState<T>.asSuccess(): ScreenState.Success<T> {
    contract {
        returns() implies (this@asSuccess is ScreenState.Success)
    }
    return this as ScreenState.Success
}

internal inline fun <reified T> ScreenState<T>.success(): T = asSuccess().data

internal inline fun <reified T> ScreenState<T>.asError(): String = (this as ScreenState.Error).message
