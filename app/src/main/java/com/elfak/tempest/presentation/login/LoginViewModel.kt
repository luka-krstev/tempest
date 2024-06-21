package com.elfak.tempest.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.common.Validate
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.utility.Response
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val authRepository = AuthRepository()
    var state by mutableStateOf(LoginFormState())
        private set

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged -> {
                state = state.copy(emailError = "", email = event.email)
            }
            is LoginFormEvent.PasswordChanged -> {
                state = state.copy(passwordError = "", password = event.password)
            }
            is LoginFormEvent.Submit -> {
                onSubmit()
            }
        }
    }

    private fun onSubmit() {
        var error = false
        if (!Validate.isEmail(state.email)) {
            state = state.copy(emailError = "Invalid email address")
            error = true
        }
        if (!Validate.isLengthBetween(state.password, 8, 32)) {
            state = state.copy(passwordError = "Password must be longer than 8 characters")
            error = true
        }

        if (error) return
        viewModelScope.launch {
            authRepository.login(state.email, state.password).collect { response ->
                state = when (response) {
                    is Response.Success -> {
                        state.copy(loading = false, success = true)
                    }

                    is Response.Failure -> {
                        state.copy(loading = false, error = "Invalid email/password")
                    }

                    is Response.Loading -> {
                        state.copy(loading = true)
                    }
                }
            }
        }
    }
}