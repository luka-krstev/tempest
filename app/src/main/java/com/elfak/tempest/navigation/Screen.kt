package com.elfak.tempest.navigation

sealed class Screen(val route: String) {
    data object Home: Screen(route = "home_screen")
    data object Login: Screen(route = "register_screen")
    data object Register: Screen(route = "login_screen")
    data object Avatar: Screen(route = "avatar_screen")
    data object AllReports: Screen(route = "all_reports_screen")
    data object Report: Screen(route = "report_screen/{latitude}/{longitude}/{uid}") {
        fun createRoute(latitude: Double, longitude: Double, uid: String? = null) = "report_screen/$latitude/$longitude/$uid"
    }
    data object ReportPreview: Screen(route = "report_preview_screen/{uid}") {
        fun createRoute(uid: String) = "report_preview_screen/$uid"
    }
    data object UserPreview: Screen(route = "user_preview_screen/{uid}") {
        fun createRoute(uid: String) = "user_preview_screen/$uid"
    }
}