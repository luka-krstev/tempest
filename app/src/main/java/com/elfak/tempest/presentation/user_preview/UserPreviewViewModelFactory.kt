package com.elfak.tempest.presentation.user_preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserPreviewViewModelFactory(
    private val id: String,
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreviewViewModel::class.java)) {
            return UserPreviewViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}