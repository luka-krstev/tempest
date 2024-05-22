package com.elfak.tempest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.navigation.SetupNavGraph
import com.elfak.tempest.presentation.shared.preferences.AuthPreferences
import com.elfak.tempest.presentation.shared.view_models.AuthViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthPreferences.init(this)
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            SetupNavGraph(navController = navController)
            val userID = AuthPreferences.getUserID()
            if (userID != null) {
                navController.navigate(Screen.Home.route)
            }
        }
    }
}
