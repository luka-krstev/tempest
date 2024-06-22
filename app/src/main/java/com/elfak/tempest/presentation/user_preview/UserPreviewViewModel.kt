package com.elfak.tempest.presentation.user_preview

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

class UserPreviewViewModel(private val id: String): ViewModel() {
    private val userRepository = UserRepository()

    var user by mutableStateOf(User())

    init {
        load()
    }

    private fun load() {
        userRepository.getById(id)
            .catch { exception -> exception.printStackTrace() }
            .onEach { response ->
                when(response) {
                    is Response.Success -> {
                        response.data?.let {
                           user = it
                        }
                    }
                    else -> { }
                }
            }
            .launchIn(viewModelScope)
    }
}