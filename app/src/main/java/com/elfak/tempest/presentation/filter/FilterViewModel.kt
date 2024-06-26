package com.elfak.tempest.presentation.filter

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

class FilterViewModel: ViewModel() {
    private val userRepository = UserRepository()

    var state by mutableStateOf(FilterState())
        private set

    var authors by mutableStateOf<List<User>>(emptyList())
        private set

    init {
        load()
    }

    private fun load() {
        userRepository.get()
            .catch { exception -> exception.printStackTrace() }
            .onEach { response ->
                when(response) {
                    is Response.Success -> {
                        authors = response.data
                    }
                    else -> { }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: FilterEvent) {
        state = when (event) {
            is FilterEvent.TitleChanged -> {
                state.copy(title = event.title)
            }
            is FilterEvent.RadiusChanged -> {
                state.copy(radius = event.radius)
            }
            is FilterEvent.PriorityChanged -> {
                state.copy(priority = event.priority)
            }
            is FilterEvent.AuthorChanged -> {
                state.copy(author = event.author)
            }
            is FilterEvent.ContentChanged -> {
                state.copy(content = event.content)
            }
            is FilterEvent.CreatedChanged -> {
                state.copy(created = event.created)
            }
        }
    }
}