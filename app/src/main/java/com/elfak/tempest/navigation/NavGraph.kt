package com.elfak.tempest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elfak.tempest.presentation.avatar.AvatarScreen
import com.elfak.tempest.presentation.home.HomeScreen
import com.elfak.tempest.presentation.login.LoginScreen
import com.elfak.tempest.presentation.register.RegisterScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(route = Screen.Avatar.route) {
            AvatarScreen(navController)
        }
    }
}