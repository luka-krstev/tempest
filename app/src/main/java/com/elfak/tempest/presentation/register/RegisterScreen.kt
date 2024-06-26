package com.elfak.tempest.presentation.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elfak.tempest.utility.navigation.Screen
import com.elfak.tempest.common.components.Button
import com.elfak.tempest.common.components.Input
import com.elfak.tempest.common.components.Title

@Composable
fun RegisterScreen(navController: NavController) {
    val registerViewModel = viewModel<RegisterViewModel>()
    val state = registerViewModel.state

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.navigate(Screen.Avatar.route)
        }
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .padding(top = 24.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Title(
            title = "Welcome to Tempest",
            description = "Join Tempest to effortlessly manage and optimize your fiber optic network."
        )
        Spacer(modifier = Modifier.height(28.dp))
        Column {
            Input(value = state.username, label = "Username", error = state.usernameError) {
                registerViewModel.onEvent(RegisterFormEvent.UsernameChanged(it))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = state.fullName, label = "Full name", error = state.fullNameError) {
                registerViewModel.onEvent(RegisterFormEvent.FullNameChanged(it))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = state.phone, label = "Phone", error = state.phoneError) {
                registerViewModel.onEvent(RegisterFormEvent.PhoneChanged(it))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = state.email, label = "Email", error = state.emailError) {
                registerViewModel.onEvent(RegisterFormEvent.EmailChanged(it))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Input(value = state.password, label = "Password", type = "password", error = state.passwordError) {
                registerViewModel.onEvent(RegisterFormEvent.PasswordChanged(it))
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Spacer(modifier = Modifier.height(8.dp))

        Column {
            Button(
                text = "Register",
                loading = state.loading,
                onClick = {
                    registerViewModel.onEvent(RegisterFormEvent.Submit)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(text = "Already have an Account?", type="secondary", onClick = {
                navController.navigate(Screen.Login.route)
            })
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    RegisterScreen(navController = rememberNavController())
}