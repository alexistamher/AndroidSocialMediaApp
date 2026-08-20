package dev.spooky.socialmediaapp.presentation.screens.login

import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.core.util.error
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.domain.usecase.auth.LoginUseCase
import dev.spooky.socialmediaapp.presentation.screens.BaseViewModel
import dev.spooky.socialmediaapp.presentation.util.ScreenState
import kotlinx.coroutines.launch

internal class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val sessionHelper: SessionHelper,
) : BaseViewModel<ScreenState<Unit>>() {
    override fun initialState(): ScreenState<Unit> = ScreenState.Idle

    lateinit var onLoginSuccess: () -> Unit

    fun checkSession() =
        viewModelScope.launch {
            val validSession = sessionHelper.validateSession()
            if (validSession) {
                onLoginSuccess()
            }
        }

    fun login(
        email: String,
        password: String,
    ) {
        setState { ScreenState.Loading() }
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            if (result.isFailure) {
                setState { ScreenState.Error(result.error()) }
                return@launch
            }
            setState { ScreenState.Success(Unit) }.join()
            onLoginSuccess()
        }
    }

    fun resetError() {
        setState { ScreenState.Idle }
    }
}
