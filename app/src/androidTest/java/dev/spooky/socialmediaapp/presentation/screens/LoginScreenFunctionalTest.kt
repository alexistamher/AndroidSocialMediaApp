package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenFunctionalTest {
    @get:Rule
    val rule = createComposeRule()

    val emailField = hasTestTag("email_field")
    val passwordField = hasTestTag("password_field")
    val loginButton = hasTestTag("login_button")
    val mailErrorHelper = hasTestTag("email_error_helper")
    val passwordErrorHelper = hasTestTag("password_error_helper")

    @Before
    fun startup() {
        rule.setContent { LoginScreen(onNavigateToRegister = {}, onLoginSuccess = {}) }
    }

    @Test
    fun testLoginHomeDisplaysSuccessfully() {
        rule.onNode(hasTestTag("signin_screen"), useUnmergedTree = true).assertExists()
    }

    @Test
    fun testLoginScreenValidForm() {
        rule.onNode(emailField).performTextInput("jperez@mail.com")
        rule.onNode(passwordField).performTextInput("Qwerty123")

        rule.onNode(loginButton).assertIsEnabled()
    }


    @Test
    fun testCheckEmailValidationWorks() = runTest {
        rule.onNode(emailField).performTextInput("jperez")
        rule.onNode(passwordField).performTextInput("Qwerty123")

        rule.onNode(mailErrorHelper, useUnmergedTree = true).assertExists()
        rule.onNode(loginButton).assertIsNotEnabled()

        rule.onNode(emailField).performTextInput("@mail.com")

        rule.onNode(mailErrorHelper, useUnmergedTree = true).assertDoesNotExist()
        rule.onNode(loginButton).assertIsEnabled()
    }

    @Test
    fun testChekPasswordValidationWorks()  {
        rule.onNode(emailField).performTextInput("jperez@mail.com")
        rule.onNode(passwordField).performTextInput("qwerty123")

        rule.onNode(passwordErrorHelper, useUnmergedTree = true).assertExists()
        rule.onNode(loginButton).assertIsNotEnabled()

        rule.onNode(passwordField).performTextInput("Q")

        rule.onNode(passwordErrorHelper, useUnmergedTree = true).assertDoesNotExist()
        rule.onNode(loginButton).assertIsEnabled()
    }
}