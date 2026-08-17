package dev.spooky.socialmediaapp.presentation.screens.register

import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.core.util.error
import dev.spooky.socialmediaapp.domain.usecase.auth.RegisterUseCase
import dev.spooky.socialmediaapp.presentation.screens.BaseViewModel
import dev.spooky.socialmediaapp.presentation.util.ScreenState
import kotlinx.coroutines.launch

internal class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : BaseViewModel<ScreenState<Unit>>() {

    override fun initialState() = ScreenState.Idle
    lateinit var onRegisterSuccess: () -> Unit

    fun register(username: String, displayName: String, email: String, password: String) {
        setState { ScreenState.Loading }
        viewModelScope.launch {
            val result = registerUseCase(username, displayName, email, password)
            if (result.isFailure) {
                setState { ScreenState.Error(result.error()) }
                return@launch
            }
            setState { ScreenState.Success(Unit) }
            onRegisterSuccess()
        }
    }

    fun resetError() {
        setState { ScreenState.Idle }
    }

}