package dev.spooky.socialmediaapp.presentation.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.spooky.socialmediaapp.presentation.FormError
import dev.spooky.socialmediaapp.presentation.util.ScreenState
import dev.spooky.socialmediaapp.presentation.util.isEmailValid
import dev.spooky.socialmediaapp.presentation.util.isPasswordValid
import dev.spooky.socialmediaapp.ui.theme.SocialMediaAppTheme
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinInject<LoginViewModel>(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var formErrors by remember { mutableStateOf(emptyMap<FormError, String>()) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val formValid by remember { derivedStateOf { formErrors.isEmpty() && email.isNotEmpty() && password.isNotEmpty() } }

    LaunchedEffect(Unit) {
        viewModel.onLoginSuccess = onLoginSuccess
        viewModel.checkSession()
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
    }

    fun login() {
        viewModel.login(email, password)
    }

    Scaffold(Modifier.semantics {
        testTagsAsResourceId = true
        testTag = "signin_screen"
    }, snackbarHost = {
        if (state !is ScreenState.Error) return@Scaffold
        val error = (state as ScreenState.Error).message
        ModalBottomSheet(
            onDismissRequest = viewModel::resetError,
            Modifier.semantics {
                testTag = "login_bottom_sheet"
            }
        ) {
            Text(error)
        }
    }) { mainPadding ->
        if (state is ScreenState.Loading) {
            LinearProgressIndicator(
                Modifier
                    .testTag("login:progress_bar")
                    .fillMaxWidth()
                    .systemBarsPadding()
            )
        }
        Column(
            Modifier
                .padding(mainPadding)
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically),
        ) {
            Text("Login", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                email, onValueChange = ::onEmailChange,
                Modifier
                    .semantics {
                        testTag = "login:email_field"
                    }
                    .fillMaxWidth(),
                label = {
                    Text("Email")
                },
                isError = FormError.EMAIL_FORMAT in formErrors,
                supportingText = {
                    val message = formErrors[FormError.EMAIL_FORMAT] ?: return@OutlinedTextField
                    Text(message, Modifier.testTag("email_error_helper"))
                },
            )

            OutlinedTextField(
                password, onValueChange = ::onPasswordChange,
                Modifier
                    .semantics {
                        testTag = "login:password_field"
                    }
                    .fillMaxWidth(),
                label = {
                    Text("Password")
                },
                isError = FormError.PASSWORD_FORMAT in formErrors,
                supportingText = {
                    val message = formErrors[FormError.PASSWORD_FORMAT] ?: return@OutlinedTextField
                    Text(message, Modifier.testTag("password_error_helper"))
                },
            )

            Button(
                ::login,
                Modifier
                    .semantics {
                        testTag = "login_button"
                    }
                    .fillMaxWidth(),
                enabled = formValid && state is ScreenState.Idle,
            ) {
                Text(
                    "Login",
                    Modifier
                        .padding(12.dp),
                )
            }

            TextButton(
                onNavigateToRegister, Modifier
                    .padding(top = 12.dp)
                    .semantics {
                        testTag = "navigate_to_signup_button"
                    }) {
                Text("Don't have an account? Sign up")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoginScree() = SocialMediaAppTheme(dynamicColor = false) {
    LoginScreen(onNavigateToRegister = {}, onLoginSuccess = {})
}

