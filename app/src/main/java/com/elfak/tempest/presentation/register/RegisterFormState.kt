package com.elfak.tempest.presentation.register

data class RegisterFormState(
    val username: String = "",
    val usernameError: String = "",
    val fullName: String = "",
    val fullNameError: String = "",
    val phone: String = "",
    val phoneError: String = "",
    val email: String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val loading: Boolean = false,
    val success: Boolean = false,
)
