package dev.spooky.socialmediaapp.presentation.screens

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 24)
class LoginScreenAutomatedTest {
    private lateinit var device: UiDevice
    private val appPackage = "dev.spooky.socialmediaapp"
    private val timeout = 5_000L

    @Before
    fun startApp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = context.packageManager.getLaunchIntentForPackage(appPackage)
        intent?.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg(appPackage).depth(0)), timeout)
    }

    @Test
    fun loginWithValidCredentials() {
        val emailField = device.wait(
            Until.findObject(By.desc("email text field")), timeout
        )
        emailField.text = "jperez@mail.com"

        val passwordField = device.wait(
            Until.findObject(By.desc("password text field")), timeout
        )
        passwordField.text = "Qwerty123"

        val loginButton = device.wait(
            Until.findObject(By.desc("signin button")), timeout
        )

        assert(loginButton.isEnabled)

        loginButton.click()

    }
}
