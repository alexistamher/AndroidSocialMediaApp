package dev.spooky.socialmediaapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.spooky.socialmediaapp.presentation.screens.home.HomeScreen
import dev.spooky.socialmediaapp.presentation.screens.login.LoginScreen
import dev.spooky.socialmediaapp.presentation.screens.register.RegisterScreen

@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login,
    ) {
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
