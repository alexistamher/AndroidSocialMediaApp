package dev.spooky.socialmediaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.spooky.socialmediaapp.presentation.navigation.RootNavigation
import dev.spooky.socialmediaapp.ui.theme.SocialMediaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialMediaAppTheme(darkTheme = false) {
                RootNavigation()
            }
        }
    }
}
