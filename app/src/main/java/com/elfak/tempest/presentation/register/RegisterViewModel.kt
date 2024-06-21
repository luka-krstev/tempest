package com.elfak.tempest.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.common.Validate
import com.elfak.tempest.model.User
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.repository.UserRepository
import com.elfak.tempest.utility.Response
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    var state by mutableStateOf(RegisterFormState())
        private set

    fun onEvent(event: RegisterFormEvent) {
        when (event) {
            is RegisterFormEvent.UsernameChanged -> {
                state = state.copy(usernameError = "", username = event.username)
            }
            is RegisterFormEvent.FullNameChanged -> {
                state = state.copy(fullNameError = "", fullName = event.fullName)
            }
            is RegisterFormEvent.PhoneChanged -> {
                state = state.copy(phoneError = "", phone = event.phone)
            }
            is RegisterFormEvent.EmailChanged -> {
                state = state.copy(emailError = "", email = event.email)
            }
            is RegisterFormEvent.PasswordChanged -> {
                state = state.copy(passwordError = "", password = event.password)
            }
            is RegisterFormEvent.Submit -> {
                onSubmit()
            }
        }
    }

    private fun onSubmit() {
        var error = false
        if (!Validate.isLengthBetween(state.username, 4, 64)) {
            state = state.copy(usernameError = "Username must be longer than 4 characters")
            error = true
        }
        if (!Validate.isPhoneNumber(state.phone)) {
            state = state.copy(phoneError = "Must be a valid phone number")
            error = true
        }
        if (!Validate.isLengthBetween(state.fullName, 4, 64)) {
            state = state.copy(fullNameError = "Full name must be longer than 4 characters")
            error = true
        }
        if (!Validate.isEmail(state.email)) {
            state = state.copy(emailError = "Must be a valid email address")
            error = true
        }
        if (!Validate.isLengthBetween(state.password, 8, 32)) {
            state = state.copy(passwordError = "Password must be longer than 8 characters")
            error = true
        }

        if (error) return
        viewModelScope.launch {
            authRepository.register(state.email, state.password).collect { response ->
                when (response) {
                    is Response.Success -> {
                        userRepository.save(User(
                            email = state.email,
                            fullName = state.fullName,
                            phone = state.phone,
                            username = state.username,
                        )).collect { inner ->
                            state = when (inner) {
                                is Response.Success -> {
                                    state.copy(loading = false, success = true)
                                }
                                is Response.Failure -> {
                                    state.copy(loading = false)
                                }

                                is Response.Loading -> {
                                    state.copy(loading = true)
                                }
                            }
                        }
                    }

                    is Response.Failure -> {
                        state = state.copy(loading = false)
                    }

                    is Response.Loading -> {
                        state = state.copy(loading = true)
                    }
                }
            }
        }
    }
}