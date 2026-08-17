package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performTextInput
import dev.spooky.socialmediaapp.presentation.screens.register.RegisterScreen
import dev.spooky.socialmediaapp.ui.theme.SocialMediaAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterScreenFunctionalTest {
    @get:Rule
    val rule = createComposeRule()

    val usernameField = hasTestTag("signup:username_field")
    val displayNameField = hasTestTag("display_name_field")
    val emailField = hasTestTag("signup:email_field")
    val passwordField = hasTestTag("signup:password_field")
    val repeatPasswordField = hasTestTag("repeat_password_field")
    val signUpButton = hasTestTag("signup_button")

    @Before
    fun startup() {
        rule.setContent {
            SocialMediaAppTheme {
                RegisterScreen(
                    onNavigateToLogin = {},
                    onRegisterSuccess = {})
            }
        }
    }

    @Test
    fun testRegisterHomeDisplaysSuccessfully() {
        rule.onNode(hasTestTag("signup_screen")).assertExists()
    }

    @Test
    fun testRegisterScreenValidForm() {
        rule.onNode(displayNameField).performTextInput("John Connor")
        rule.onNode(usernameField).performTextInput("jconnor1")
        rule.onNode(emailField).performTextInput("jconnor@mail.com")
        rule.onNode(passwordField).performTextInput("Qwerty123")
        rule.onNode(repeatPasswordField).performTextInput("Qwerty123")

        rule.onNode(signUpButton).assertIsEnabled()
    }

    @Test
    fun testDisplayNameErrorMessageDisplays() {
        val errorHelper = hasTestTag("display_name_error_helper")

        rule.onNode(displayNameField).performTextInput("John")
        rule.onNode(usernameField).performTextInput("jconnor1")
        rule.onNode(emailField).performTextInput("jconnor@mail.com")
        rule.onNode(passwordField).performTextInput("Qwerty123")
        rule.onNode(repeatPasswordField).performTextInput("Qwerty123")

        rule.onNode(errorHelper, useUnmergedTree = true).assertExists()
        rule.onNode(signUpButton).assertIsNotEnabled()

        rule.onNode(displayNameField).performTextInput(" Connor")

        rule.onNode(errorHelper, useUnmergedTree = true).assertDoesNotExist()
        rule.onNode(signUpButton).assertIsEnabled()
    }

    @Test
    fun testUsernameErrorMessageDisplays() {
        val errorHelper = hasTestTag("username_error_helper")

        rule.onNode(displayNameField).performTextInput("John Connor")
        rule.onNode(usernameField).performTextInput("jconnor")
        rule.onNode(emailField).performTextInput("jconnor@mail.com")
        rule.onNode(passwordField).performTextInput("Qwerty123")
        rule.onNode(repeatPasswordField).performTextInput("Qwerty123")

        rule.onNode(errorHelper, useUnmergedTree = true).assertExists()
        rule.onNode(signUpButton).assertIsNotEnabled()

        rule.onNode(usernameField).performTextInput("jconnor92")

        rule.onNode(errorHelper, useUnmergedTree = true).assertDoesNotExist()
        rule.onNode(signUpButton).assertIsEnabled()
    }

    @Test
    fun testEmailErrorMessageDisplays() {
        val errorHelper = hasTestTag("email_error_helper")

        rule.onNode(displayNameField).performTextInput("John Connor")
        rule.onNode(usernameField).performTextInput("jconnor92")
        rule.onNode(emailField).performTextInput("jconnor92")
        rule.onNode(passwordField).performTextInput("Qwerty123")
        rule.onNode(repeatPasswordField).performTextInput("Qwerty123")

        rule.onNode(errorHelper, useUnmergedTree = true).assertExists()
        rule.onNode(signUpButton).assertIsNotEnabled()

        rule.onNode(emailField).performTextInput("@mail.com")

        rule.onNode(errorHelper, useUnmergedTree = true).assertDoesNotExist()
        rule.onNode(signUpButton).assertIsEnabled()
    }

    @Test
    fun testPasswordErrorMessageDisplays() {
        val errorHelper = hasTestTag("password_error_helper")

        rule.onNode(displayNameField).performTextInput("John Connor")
        rule.onNode(usernameField).performTextInput("jconnor92")
        rule.onNode(emailField).performTextInput("jconnor92@mail.com")
        rule.onNode(passwordField).performTextInput("Qwerty")
        rule.onNode(repeatPasswordField).performTextInput("Qwerty123")

        rule.onNode(errorHelper, useUnmergedTree = true).assertExists()
        rule.onNode(signUpButton).assertIsNotEnabled()

        rule.onNode(passwordField).performTextInput("123")

        rule.onNode(errorHelper, useUnmergedTree = true).assertDoesNotExist()
        rule.onNode(signUpButton).assertIsEnabled()
    }

    @Test
    fun testRepeatPasswordErrorMessageDisplays() {
        val errorHelper = hasTestTag("repeat_password_error_helper")

        rule.onNode(displayNameField).performTextInput("John Connor")
        rule.onNode(usernameField).performTextInput("jconnor92")
        rule.onNode(emailField).performTextInput("jconnor92@mail.com")
        rule.onNode(passwordField).performTextInput("Qwerty123")
        rule.onNode(repeatPasswordField).performTextInput("Qwerty")

        rule.onNode(errorHelper, useUnmergedTree = true).assertExists()
        rule.onNode(signUpButton).assertIsNotEnabled()

        rule.onNode(repeatPasswordField).performTextInput("123")

        rule.onNode(errorHelper, useUnmergedTree = true).assertDoesNotExist()
        rule.onNode(signUpButton).assertIsEnabled()
    }
}