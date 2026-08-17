package dev.spooky.socialmediaapp.presentation.screens

import androidx.test.uiautomator.By
import androidx.test.uiautomator.uiAutomator
import androidx.test.uiautomator.waitForStable
import org.junit.Test

class LoginScreenAutomatedTest {

    @Test
    fun should_login_successfully() = uiAutomator {
        startApp("dev.spooky.socialmediaapp")

        val emailField = device.findObject(By.res("login:email_field"))
        emailField.text = "example@mail.com"

        val passwordField = device.findObject(By.res("login:password_field"))
        passwordField.text = "Qwerty123"

        device.findObject(By.res("login_button")).click()

        onElement { contentDescription == "home screen" }.waitForStable()
    }
}
