package com.elfak.tempest.presentation.ticket_preview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.model.Ticket
import com.elfak.tempest.repository.TicketRepository
import com.elfak.tempest.utility.Response
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TicketPreviewViewModel(
    private val id: String
): ViewModel() {
    private val ticketRepository = TicketRepository()

    var ticket by mutableStateOf(Ticket())

    init {
        load()
    }

    private fun load() {
        ticketRepository.get(id)
            .catch { exception -> exception.printStackTrace() }
            .onEach { response ->
                when(response) {
                    is Response.Success -> {
                        response.data?.let{
                            ticket = it
                        }
                    }
                    else -> { }
                }
            }
            .launchIn(viewModelScope)
    }

    fun solve(id: String, onSuccess: () -> Unit) {
        ticketRepository.solve(id, onSuccess)
    }
}