package dev.spooky.socialmediaapp.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal abstract class BaseViewModel<T> : ViewModel() {
    private val _state: MutableStateFlow<T> by lazy {
        MutableStateFlow(initialState())
    }
    val state: StateFlow<T>
        get() = _state.asStateFlow()
    protected val currentState: T
        get() = _state.value

    protected abstract fun initialState(): T

    protected fun setState(state: (T) -> T) =
        viewModelScope.launch(Dispatchers.Main) {
            _state.update { state.invoke(it) }
        }
}
