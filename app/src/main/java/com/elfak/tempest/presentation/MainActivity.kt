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
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthPreferences.init(this)
        AvatarPreferences.init(this)
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            SetupNavGraph(navController = navController)

            val userID = AuthPreferences.getUserID()
            if (userID != null) {
                val avatar = AvatarPreferences.getExists()
                if (!avatar) {
                    navController.navigate(Screen.Avatar.route)
                } else {
                    navController.navigate(Screen.Home.route)
                }
            } else {
                navController.navigate(Screen.Login.route)
            }
        }
    }
}
