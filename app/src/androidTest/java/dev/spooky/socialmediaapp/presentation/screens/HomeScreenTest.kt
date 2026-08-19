package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.spooky.socialmediaapp.presentation.navigation.RootNavigation
import dev.spooky.socialmediaapp.ui.theme.SocialMediaAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
    @get:Rule
    val rule = createComposeRule()

    val emailField = hasTestTag("login:email_field")
    val passwordField = hasTestTag("login:password_field")
    val loginButton = hasTestTag("login_button")

    @Before
    fun startup() {
        rule.setContent {
            SocialMediaAppTheme {
                RootNavigation()
            }
        }
    }

    @Test
    fun should_login_and_display_homescreen() {
        rule.onNode(emailField).performTextInput("jconnor@mail.com")
        rule.onNode(passwordField).performTextInput("Qwerty123")

        rule.onNode(loginButton).performClick()
    }
}
