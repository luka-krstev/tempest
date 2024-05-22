package com.elfak.tempest.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.presentation.shared.components.Button
import com.elfak.tempest.presentation.shared.components.Input
import com.elfak.tempest.presentation.shared.components.Title
import com.elfak.tempest.presentation.shared.view_models.AuthViewModel

@Composable
fun LoginScreen(navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val authViewModel = viewModel<AuthViewModel>()
    val authState = authViewModel.state

    Column(
        modifier = Modifier
            .padding(24.dp)
            .padding(top = 24.dp)
            .fillMaxSize(),
    ) {
        Title(
            title = "Welcome back!",
            description = "Weave Your Fiber Network Fabric with Ease"
        )
        Spacer(modifier = Modifier.height(48.dp))
        Column {
            Input(value = email, label = "Email") {
                email = it
            }
            Spacer(modifier = Modifier.height(8.dp))
            Input(value = password, label = "Password", type = "password") {
                password = it
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Column {
            Button(text = "Login", onClick = {
                authViewModel.signIn(email, password)
            })
            Spacer(modifier = Modifier.height(8.dp))
            Button(text = "Don't have an Account?", type="secondary", onClick = {
                navController.navigate(Screen.Register.route)
            })
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Success -> {
                navController.navigate(Screen.Home.route)
            }
            else -> { }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}