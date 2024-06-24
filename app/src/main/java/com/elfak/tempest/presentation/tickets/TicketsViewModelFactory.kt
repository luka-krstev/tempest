package com.elfak.tempest.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TicketsViewModelFactory(
    private val location: Pair<Double, Double>?,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicketsViewModel::class.java)) {
            return TicketsViewModel(location) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}