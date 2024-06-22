package com.elfak.tempest.presentation.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.model.User
import com.elfak.tempest.repository.UserRepository
import com.elfak.tempest.utility.Response
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UsersViewModel: ViewModel() {
    private val userRepository = UserRepository()

    var users by mutableStateOf<List<User>>(emptyList())

    init {
        load()
    }

    private fun load() {
        userRepository.get()
            .catch { exception -> exception.printStackTrace() }
            .onEach { response ->
                when(response) {
                    is Response.Success -> {
                        users = response.data
                    }
                    else -> { }
                }
            }
            .launchIn(viewModelScope)
    }
}