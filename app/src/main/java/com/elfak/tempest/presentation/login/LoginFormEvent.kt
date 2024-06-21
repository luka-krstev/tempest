package com.elfak.tempest.presentation.login

sealed class LoginFormEvent {
    data class EmailChanged(val email: String): LoginFormEvent()
    data class PasswordChanged(val password: String): LoginFormEvent()
    data object Submit: LoginFormEvent()
}
