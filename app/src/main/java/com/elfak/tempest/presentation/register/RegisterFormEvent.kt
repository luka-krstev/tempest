package com.elfak.tempest.presentation.register

sealed class RegisterFormEvent {
    data class UsernameChanged(val username: String): RegisterFormEvent()
    data class FullNameChanged(val fullName: String): RegisterFormEvent()
    data class PhoneChanged(val phone: String): RegisterFormEvent()
    data class EmailChanged(val email: String): RegisterFormEvent()
    data class PasswordChanged(val password: String): RegisterFormEvent()
    data object Submit: RegisterFormEvent()
}