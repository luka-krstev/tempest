package com.elfak.tempest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.elfak.tempest.utility.navigation.Screen
import com.elfak.tempest.utility.navigation.SetupNavGraph
import com.elfak.tempest.repository.AuthRepository

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val authRepository = AuthRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            navController = rememberNavController()
            val user = authRepository.current()
            if (user != null) {
                SetupNavGraph(navController = navController, startDestination = Screen.Home.route)
            } else {
                SetupNavGraph(navController = navController, startDestination = Screen.Login.route)
            }
        }
    }
}
