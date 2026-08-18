package dev.spooky.socialmediaapp.integration

import androidx.test.uiautomator.By
import androidx.test.uiautomator.uiAutomator
import androidx.test.uiautomator.waitForStable
import org.junit.Test

class O1RegisterScreenAutomatedTest {
    val usernameTag = "signup:username_field"
    val displayNameTag = "display_name_field"
    val emailTag = "signup:email_field"
    val passwordTag = "signup:password_field"
    val repeatPasswordTag = "repeat_password_field"
    val signUpButtonTag = "signup_button"
    val navigateToSignupButtonTag = "navigate_to_signup_button"

    @Test
    fun should_register_a_new_user() =
        uiAutomator {
            startApp("dev.spooky.socialmediaapp")

            device.findObject(By.res(navigateToSignupButtonTag)).click()

            onElement { viewIdResourceName == usernameTag }.waitForStable()

            device.findObject(By.res(usernameTag)).text = "JConnor92"
            device.findObject(By.res(displayNameTag)).text = "John Connor"
            device.findObject(By.res(emailTag)).text = "jconnor@mail.com"
            device.findObject(By.res(passwordTag)).text = "Qwerty123"
            device.findObject(By.res(repeatPasswordTag)).text = "Qwerty123"

            device.findObject(By.res(signUpButtonTag)).click()

            onElement { viewIdResourceName == "home_screen" }.waitForStable()
        }
}
