package com.elfak.tempest.presentation.modify_ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ModifyTicketViewModelFactory(
    private val id: String?,
    private val latitude: Double,
    private val longitude: Double,
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModifyTicketViewModel::class.java)) {
            return ModifyTicketViewModel(id, latitude, longitude) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}