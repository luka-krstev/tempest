package com.elfak.tempest.presentation.modify_ticket

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.common.Validate
import com.elfak.tempest.model.Ticket
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.repository.TicketRepository
import com.elfak.tempest.utility.Response
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ModifyTicketViewModel(
    private val id: String?,
    private val latitude: Double,
    private val longitude: Double,
): ViewModel() {
    private val ticketRepository = TicketRepository()
    private val authRepository = AuthRepository()

    var state by mutableStateOf(ModifyTicketFormState())
        private set

    fun onEvent(event: ModifyTicketFormEvent) {
        when (event) {
            is ModifyTicketFormEvent.TitleChanged -> {
                state = state.copy(titleError = "", title = event.title)
            }
            is ModifyTicketFormEvent.ContentChanged -> {
                state = state.copy(contentError = "", content = event.content)
            }
            is ModifyTicketFormEvent.PriorityChanged -> {
                state = state.copy(priority = event.priority)
            }
            is ModifyTicketFormEvent.Submit -> {
                onSubmit()
            }
        }
    }

    init {
        load()
    }

    private fun onSubmit() {
        var error = false
        if (!Validate.isLengthBetween(state.content, 1, 256)) {
            state = state.copy(contentError = "Content cannot be empty")
            error = true
        }
        if (!Validate.isLengthBetween(state.title, 1, 256)) {
            state = state.copy(contentError = "Title cannot be empty")
            error = true
        }

        if (error) return
        authRepository.current()?.let { user ->
            user.email?.let { email ->
                viewModelScope.launch {
                    ticketRepository.save(Ticket(
                        id = id ?: "",
                        title = state.title,
                        content = state.content,
                        priority = state.priority,
                        latitude = latitude,
                        longitude = longitude,
                        userEmail = email,
                    )).collect { response ->
                        state = when (response) {
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
            }
        }
    }

    private fun load() {
        if (id != null) {
            ticketRepository.get(id)
                .catch { exception -> exception.printStackTrace() }
                .onEach { response ->
                    when(response) {
                        is Response.Success -> {
                            response.data?.let {
                                state = state.copy(
                                    title = it.title,
                                    content = it.content,
                                    priority = it.priority
                                )
                            }
                        }
                        else -> { }
                    }
                }
                .launchIn(viewModelScope)
        }
    }
}