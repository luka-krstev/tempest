package com.elfak.tempest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.navigation.SetupNavGraph
import com.elfak.tempest.presentation.shared.preferences.AuthPreferences
import com.elfak.tempest.presentation.shared.preferences.AvatarPreferences
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.repository.UserRepository
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            navController = rememberNavController()
            val user = authRepository.current()
            if (user != null) {
                SetupNavGraph(navController = navController, startDestination = Screen.Avatar.route)
            } else {
                SetupNavGraph(navController = navController, startDestination = Screen.Login.route)
            }
        }
    }
}
