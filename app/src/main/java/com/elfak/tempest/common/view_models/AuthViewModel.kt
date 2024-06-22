package com.elfak.tempest.common.view_models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.model.User
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.repository.UserRepository
import com.elfak.tempest.utility.Response
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    var current by mutableStateOf<User?>(null)
        private set

    fun logout() {
        authRepository.logout()
        current = null
    }

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val uid = authRepository.current()
        uid?.let { user ->
            user.email?.let { email ->
                viewModelScope.launch {
                    userRepository.getByEmail(email).collect { response ->
                        when (response) {
                            is Response.Success -> {
                                current = response.data
                            }
                            else -> { }
                        }
                    }
                }
            }
        }
    }
}