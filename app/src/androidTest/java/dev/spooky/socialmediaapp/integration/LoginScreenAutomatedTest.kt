package dev.spooky.socialmediaapp.integration

import androidx.test.uiautomator.By
import androidx.test.uiautomator.uiAutomator
import androidx.test.uiautomator.waitForStable
import org.junit.Test

class LoginScreenAutomatedTest {
    private val usernameTag = "login:email_field"

    @Test
    fun should_login_successfully() =
        uiAutomator {
            startApp("dev.spooky.socialmediaapp")

            onElement { viewIdResourceName == usernameTag }.waitForStable()
            val emailField = device.findObject(By.res(usernameTag))
            emailField.text = "jconnor@mail.com"

            val passwordField = device.findObject(By.res("login:password_field"))
            passwordField.text = "Qwerty123"

            device.findObject(By.res("login_button")).click()

            onElement { viewIdResourceName == "home_screen" }.waitForStable()
        }
}
