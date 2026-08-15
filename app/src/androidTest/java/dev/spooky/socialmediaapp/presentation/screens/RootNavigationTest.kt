package dev.spooky.socialmediaapp.presentation.screens

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import dev.spooky.socialmediaapp.presentation.navigation.RootNavigation
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RootNavigationTest {
    @get:Rule
    val rule = createComposeRule()

    @Before
    fun startup() {
        rule.setContent { RootNavigation() }
    }

    @Test
    fun testNavigationToRegister() {
        rule.onNode(hasTestTag("signin_screen")).assertExists()
        rule.onNode(hasTestTag("navigate_to_signup_button")).performClick()

        rule.onNode(hasTestTag("signup_screen")).assertExists()

        rule.onNode(hasTestTag("navigate_to_signin_button")).performClick()

        rule.onNode(hasTestTag("signin_screen")).assertExists()
    }
}