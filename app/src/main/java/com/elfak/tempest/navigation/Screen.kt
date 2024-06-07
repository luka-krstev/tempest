package com.elfak.tempest.navigation

sealed class Screen(val route: String) {
    data object Home: Screen(route = "home_screen")
    data object Login: Screen(route = "register_screen")
    data object Register: Screen(route = "login_screen")
    data object Avatar: Screen(route = "avatar_screen")
}