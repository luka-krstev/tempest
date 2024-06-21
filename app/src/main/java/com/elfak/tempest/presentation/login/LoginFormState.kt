package com.elfak.tempest.presentation.login

data class LoginFormState(
    val email: String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String = "",
)
