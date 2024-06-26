package com.elfak.tempest.utility.navigation

sealed class Screen(val route: String) {
    data object Home: Screen(route = "home_screen")
    data object Login: Screen(route = "register_screen")
    data object Register: Screen(route = "login_screen")
    data object Avatar: Screen(route = "avatar_screen")
    data object Tickets: Screen(route = "tickets_screen/{latitude}/{longitude}") {
        fun createRoute(latitude: Double?, longitude: Double?) = "tickets_screen/${latitude ?: 0f}/${longitude ?: 0f}"
    }
    data object Users: Screen(route = "users_screen")
    data object Filters: Screen(route = "filters_screen")
    data object ModifyTicket: Screen(route = "modify_ticket_screen/{latitude}/{longitude}/{id}") {
        fun createRoute(latitude: Double, longitude: Double, id: String? = null) = "modify_ticket_screen/$latitude/$longitude/$id"
    }
    data object TicketPreview: Screen(route = "ticket_preview_screen/{id}") {
        fun createRoute(id: String) = "ticket_preview_screen/$id"
    }
    data object UserPreview: Screen(route = "user_preview_screen/{id}") {
        fun createRoute(id: String) = "user_preview_screen/$id"
    }
}