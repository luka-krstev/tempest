package com.elfak.tempest.presentation.tickets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.model.Ticket
import com.elfak.tempest.presentation.filter.FilterState
import com.elfak.tempest.repository.TicketRepository
import com.elfak.tempest.utility.Response
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TicketsViewModel(
    private val location: Pair<Double, Double>?
): ViewModel() {
    private val ticketRepository = TicketRepository()

    var filter = FilterState()
    var tickets by mutableStateOf<List<Ticket>>(emptyList())

    init {
        load()
    }

    private fun load() {
        ticketRepository.get(filter, location)
            .catch { exception -> exception.printStackTrace() }
            .onEach { response ->
                when(response) {
                    is Response.Success -> {
                        tickets = response.data
                    }
                    else -> { }
                }
            }
            .launchIn(viewModelScope)
    }

    fun filter(filter: FilterState) {
        this.filter = filter
        load()
    }
}