package com.elfak.tempest.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elfak.tempest.presentation.avatar.AvatarScreen
import com.elfak.tempest.presentation.home.HomeScreen
import com.elfak.tempest.presentation.login.LoginScreen
import com.elfak.tempest.presentation.register.RegisterScreen
import com.elfak.tempest.presentation.report.ReportScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.Home.route,
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(600)
                )
            }
        ) {
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
        composable(
            route = Screen.Report.route,
            arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(600)
                )
            }
        ) {
            val latitude = it.arguments?.getFloat("latitude") ?: 0f
            val longitude = it.arguments?.getFloat("longitude") ?: 0f
            ReportScreen(navController, latitude, longitude)
        }
    }
}