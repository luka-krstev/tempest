package com.elfak.tempest.presentation.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.elfak.tempest.presentation.shared.preferences.AvatarPreferences
import com.elfak.tempest.presentation.shared.validators.Validators
import com.elfak.tempest.presentation.shared.view_models.AuthViewModel

fun validate(username: String, name: String, phone: String, email: String, password: String): Boolean {
    if (!Validators.isLengthBetween(username, 4, 64)) return false
    if (!Validators.isPhoneNumber(phone)) return false
    if (!Validators.isLengthBetween(name, 4, 64)) return false
    if (!Validators.isEmail(email)) return false
    if (!Validators.isLengthBetween(password, 8, 32)) return false

    return true
}

@Composable
fun RegisterScreen(navController: NavController) {
    var username by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    val valid by remember {
        derivedStateOf { validate(username, name, phone, email, password) }
    }

    val authViewModel = viewModel<AuthViewModel>()
    val authState = authViewModel.state

    Column(
        modifier = Modifier
            .padding(24.dp)
            .padding(top = 24.dp)
            .fillMaxSize(),
    ) {
        Title(
            title = "Welcome to Tempest",
            description = "Join Tempest to monitor, manage, and optimize your fiber optic network with ease."
        )
        Spacer(modifier = Modifier.height(28.dp))
        AnimatedVisibility(authState is AuthViewModel.AuthState.Error) {
            if (authState is AuthViewModel.AuthState.Error) {
                ErrorBox(authState.message)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            Input(value = username, label = "Username") {
                username = it
            }
            Spacer(modifier = Modifier.height(12.dp))
            Input(value = name, label = "Full name") {
                name = it
            }
            Spacer(modifier = Modifier.height(12.dp))
            Input(value = phone, label = "Phone") {
                phone = it
            }
            Spacer(modifier = Modifier.height(12.dp))
            Input(value = email, label = "Email") {
                email = it
            }
            Spacer(modifier = Modifier.height(12.dp))
            Input(value = password, label = "Password", type = "password") {
                password = it
            }
        }
        Spacer(modifier = Modifier.weight(1.0f))
        Column {
            Button(
                text = "Register",
                disabled = !valid,
                loading = authState == AuthViewModel.AuthState.Loading,
                onClick = {
                    authViewModel.signUp(email, password, phone, name, username)
                    AvatarPreferences.setExists(false);
                    navController.navigate(Screen.Avatar.route)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(text = "Already have an Account?", type="secondary", onClick = {
                navController.navigate(Screen.Login.route)
            })
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthViewModel.AuthState.Success -> {
                navController.navigate(Screen.Avatar.route)
            }
            else -> { }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview() {
    RegisterScreen(navController = rememberNavController())
}