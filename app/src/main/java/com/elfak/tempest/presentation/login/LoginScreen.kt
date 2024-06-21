package com.elfak.tempest.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.presentation.shared.components.Button
import com.elfak.tempest.presentation.shared.components.ErrorBox
import com.elfak.tempest.presentation.shared.components.Input
import com.elfak.tempest.presentation.shared.components.Title

@Composable
fun LoginScreen(navController: NavController) {
    val loginViewModel = viewModel<LoginViewModel>()
    val state = loginViewModel.state

    if (state.success) {
        navController.navigate(Screen.Home.route)
    }

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
        Spacer(modifier = Modifier.height(28.dp))
        AnimatedVisibility(state.error.isNotEmpty()) {
            ErrorBox(state.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            Input(value = state.email, label = "Email", error = state.emailError) {
                loginViewModel.onEvent(LoginFormEvent.EmailChanged(it))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = state.password, label = "Password", type = "password", error = state.passwordError) {
                loginViewModel.onEvent(LoginFormEvent.PasswordChanged(it))
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Column {
            Button(
                text = "Login",
                loading = state.loading,
                onClick = {
                    loginViewModel.onEvent(LoginFormEvent.Submit)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(text = "Don't have an Account?", type="secondary", onClick = {
                navController.navigate(Screen.Register.route)
            })
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}