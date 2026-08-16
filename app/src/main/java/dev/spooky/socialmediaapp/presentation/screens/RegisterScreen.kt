package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.spooky.socialmediaapp.core.util.error
import dev.spooky.socialmediaapp.domain.usecase.auth.RegisterUseCase
import dev.spooky.socialmediaapp.presentation.FormError
import dev.spooky.socialmediaapp.presentation.util.ScreenState
import dev.spooky.socialmediaapp.presentation.util.isEmailValid
import dev.spooky.socialmediaapp.presentation.util.isPasswordValid
import dev.spooky.socialmediaapp.ui.theme.SocialMediaAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = koinInject<RegisterViewModel>(),
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var formErrors by remember { mutableStateOf(emptyMap<FormError, String>()) }
    val formValid by remember {
        derivedStateOf {
            formErrors.isEmpty() && name.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
        }
    }

    fun checkPasswordMatch() {
        if (repeatPassword != password) {
            formErrors += (FormError.PASSWORD_MATCHING to "passwords does not match")
            return
        }

        formErrors = formErrors.filterNot { it.key == FormError.PASSWORD_MATCHING }
    }

    fun onDisplayNameChange(value: String) {
        name = value
        if (value.isEmpty() || value.length < 8) {
            formErrors += (FormError.DISPLAY_NAME_FORMAT to "minimum length name must be 8 characters")
            return
        }

        formErrors = formErrors.filterNot { it.key == FormError.DISPLAY_NAME_FORMAT }
    }

    fun onUsernameChange(value: String) {
        username = value
        if (value.isEmpty() || value.length < 8) {
            formErrors += (FormError.USERNAME_FORMAT to "minimum length name must be 8 characters")
            return
        }

        formErrors = formErrors.filterNot { it.key == FormError.USERNAME_FORMAT }
    }

    fun onEmailChange(value: String) {
        email = value
        if (!isEmailValid(value)) {
            formErrors += (FormError.EMAIL_FORMAT to "email does not have email format")
            return
        }

        formErrors = formErrors.filterNot { it.key == FormError.EMAIL_FORMAT }
    }

    fun onPasswordChange(value: String) {
        password = value
        if (!isPasswordValid(value)) {
            formErrors += (FormError.PASSWORD_FORMAT to "must contain digits, lowercase and uppercase letters and minimum length of 8")
            return
        }

        formErrors = formErrors.filterNot { it.key == FormError.PASSWORD_FORMAT }
        checkPasswordMatch()
    }

    fun onRepeatPasswordChange(value: String) {
        repeatPassword = value
        checkPasswordMatch()
    }

    LaunchedEffect(Unit) {
        viewModel.onRegisterSuccess = onRegisterSuccess
    }

    Scaffold(Modifier.semantics {
        testTag = "signup_screen"
    }) { mainPadding ->
        Column(
            Modifier
                .padding(mainPadding)
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        ) {
            Text("Register", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                name, onValueChange = ::onDisplayNameChange,
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        testTag = "display_name_field"
                    },
                isError = FormError.DISPLAY_NAME_FORMAT in formErrors,
                supportingText = {
                    val message = formErrors[FormError.DISPLAY_NAME_FORMAT]
                        ?: return@OutlinedTextField
                    Text(message, Modifier.testTag("display_name_error_helper"))
                },
                label = {
                    Text("Display name")
                },
            )

            OutlinedTextField(
                username, onValueChange = ::onUsernameChange,
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        testTag = "username_field"
                    },
                isError = FormError.USERNAME_FORMAT in formErrors,
                supportingText = {
                    val message = formErrors[FormError.USERNAME_FORMAT] ?: return@OutlinedTextField
                    Text(message, Modifier.testTag("username_error_helper"))
                },
                label = {
                    Text("Username")
                },
            )

            OutlinedTextField(
                email, onValueChange = ::onEmailChange,
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        testTag = "email_field"
                    },
                isError = FormError.EMAIL_FORMAT in formErrors,
                supportingText = {
                    val message = formErrors[FormError.EMAIL_FORMAT] ?: return@OutlinedTextField
                    Text(message, Modifier.testTag("email_error_helper"))
                },
                label = {
                    Text("Email")
                },
            )

            OutlinedTextField(
                password, onValueChange = ::onPasswordChange,
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        testTag = "password_field"
                    },
                isError = FormError.PASSWORD_FORMAT in formErrors,
                supportingText = {
                    val message = formErrors[FormError.PASSWORD_FORMAT] ?: return@OutlinedTextField
                    Text(message, Modifier.testTag("password_error_helper"))
                },
                label = {
                    Text("Password")
                },
            )

            OutlinedTextField(
                repeatPassword, onValueChange = ::onRepeatPasswordChange,
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        testTag = "repeat_password_field"
                    },
                isError = FormError.PASSWORD_MATCHING in formErrors,
                supportingText = {
                    val message = formErrors[FormError.PASSWORD_MATCHING]
                        ?: return@OutlinedTextField
                    Text(message, Modifier.testTag("repeat_password_error_helper"))
                },
                label = {
                    Text("Repeat password")
                },
            )

            Button(
                {
                    viewModel.register(username, name, email, password)
                },
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        testTag = "signup_button"
                    },
                enabled = formValid,
            ) {
                Text(
                    "Register", Modifier.padding(12.dp)
                )
            }

            TextButton(
                onNavigateToLogin,
                Modifier
                    .padding(top = 12.dp)
                    .semantics {
                        contentDescription = "navigate to signin screen"
                        testTag = "navigate_to_signin_button"
                    },
            ) {
                Text("Already have an account? Log in")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRegisterScreen() = SocialMediaAppTheme(dynamicColor = false) {
    RegisterScreen(onNavigateToLogin = {}, onRegisterSuccess = {})
}

internal class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<ScreenState<Unit>> = MutableStateFlow(ScreenState.Idle)
    val state: StateFlow<ScreenState<Unit>>
        get() = _state.asStateFlow()
    lateinit var onRegisterSuccess: () -> Unit

    fun register(username: String, displayName: String, email: String, password: String) {
        viewModelScope.launch {
            val result = registerUseCase(username, displayName, email, password)
            if (result.isFailure) {
                _state.update { ScreenState.Error(result.error()) }
                return@launch
            }
            _state.update { ScreenState.Success(Unit) }
            onRegisterSuccess
        }
    }

}