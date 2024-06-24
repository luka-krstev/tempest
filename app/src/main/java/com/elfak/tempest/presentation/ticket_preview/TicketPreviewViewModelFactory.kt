package com.elfak.tempest.presentation.ticket_preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TicketPreviewViewModelFactory(
    private val id: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicketPreviewViewModel::class.java)) {
            return TicketPreviewViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}