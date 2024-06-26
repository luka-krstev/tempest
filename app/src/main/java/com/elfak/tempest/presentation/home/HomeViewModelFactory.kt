package com.elfak.tempest.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.elfak.tempest.utility.location.NativeLocationClient

class HomeViewModelFactory(
    private val locationClient: NativeLocationClient,
    private val serviceActive: Boolean
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(locationClient, serviceActive) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}