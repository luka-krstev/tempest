package com.elfak.tempest.utility.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elfak.tempest.presentation.avatar.AvatarScreen
import com.elfak.tempest.presentation.filter.FilterScreen
import com.elfak.tempest.presentation.filter.FilterViewModel
import com.elfak.tempest.presentation.home.HomeScreen
import com.elfak.tempest.presentation.login.LoginScreen
import com.elfak.tempest.presentation.modify_ticket.ModifyTicketScreen
import com.elfak.tempest.presentation.register.RegisterScreen
import com.elfak.tempest.presentation.ticket_preview.TicketPreviewScreen
import com.elfak.tempest.presentation.tickets.TicketsScreen
import com.elfak.tempest.presentation.user_preview.UserPreviewScreen
import com.elfak.tempest.presentation.users.UsersScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    val filterViewModel = viewModel<FilterViewModel>()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
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
            },
        ) {
            HomeScreen(navController, filterViewModel)
        }
        composable(
            route = Screen.Login.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            LoginScreen(navController)
        }
        composable(
            route = Screen.Register.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            RegisterScreen(navController)
        }
        composable(
            route = Screen.Avatar.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            AvatarScreen(navController)
        }
        composable(
            route = Screen.Filters.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            FilterScreen(navController, filterViewModel)
        }
        composable(
            route = Screen.Tickets.route,
            arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType },
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            val latitude = it.arguments?.getFloat("latitude") ?: 0.0
            val longitude = it.arguments?.getFloat("longitude") ?: 0.0
            val location = Pair(latitude.toDouble(), longitude.toDouble())
            TicketsScreen(navController, filterViewModel, location)
        }
        composable(
            route = Screen.Users.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            UsersScreen(navController)
        }
        composable(
            route = Screen.ModifyTicket.route,
            arguments = listOf(
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType },
                navArgument("id") { type = NavType.StringType; nullable = true; defaultValue = null }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            val latitude = it.arguments?.getFloat("latitude") ?: 0.0
            val longitude = it.arguments?.getFloat("longitude") ?: 0.0
            val id = it.arguments?.getString("id")
            ModifyTicketScreen(navController, latitude.toDouble(), longitude.toDouble(), id)
        }
        composable(
            route = Screen.TicketPreview.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            val id = it.arguments?.getString("id") ?: ""
            TicketPreviewScreen(navController, id)
        }
        composable(
            route = Screen.UserPreview.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
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
            val id = it.arguments?.getString("id") ?: ""
            UserPreviewScreen(navController, id)
        }
    }
}