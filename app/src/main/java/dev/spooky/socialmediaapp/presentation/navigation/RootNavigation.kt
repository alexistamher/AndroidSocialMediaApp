package dev.spooky.socialmediaapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.spooky.socialmediaapp.data.util.SessionHelper
import dev.spooky.socialmediaapp.presentation.screens.SplashScreen
import dev.spooky.socialmediaapp.presentation.screens.home.HomeScreen
import dev.spooky.socialmediaapp.presentation.screens.login.LoginScreen
import dev.spooky.socialmediaapp.presentation.screens.register.RegisterScreen
import dev.spooky.socialmediaapp.presentation.util.LocalUserInfo
import org.koin.compose.koinInject

@Composable
fun RootNavigation() {
    val helper = koinInject<SessionHelper>()
    val userInfo by helper.userInfo.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash,
    ) {
        composable<Screen.Splash> {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
            )
        }
        composable<Screen.Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
            )
        }
        composable<Screen.Register> {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login)
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
            )
        }
        composable<Screen.Home> {
            if (userInfo == null) return@composable
            CompositionLocalProvider(LocalUserInfo provides userInfo!!) {
                HomeScreen(
                    onLogout = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Home) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}
