package com.elfak.tempest.presentation.avatar

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.repository.StorageRepository
import com.elfak.tempest.repository.UserRepository
import com.elfak.tempest.utility.Response
import kotlinx.coroutines.launch

class AvatarViewModel: ViewModel() {
    private val storageRepository = StorageRepository()
    private val userRepository = UserRepository()
    private val authRepository = AuthRepository()

    var state by mutableStateOf(AvatarFormState())
        private set

    fun onEvent(event: AvatarFormEvent) {
        when (event) {
            is AvatarFormEvent.UriChanged -> {
                state = state.copy(uri = event.uri)
            }
            is AvatarFormEvent.Submit -> {
                onSubmit()
            }
        }
    }

    fun pick(uri: Uri) {
        state = state.copy(selected = true, uri = uri)
    }

    private fun onSubmit() {
        val user = authRepository.current()
        user?.let {
            val path = "images/${user.uid}/avatar.jpg"
            viewModelScope.launch {
                storageRepository.upload(path, state.uri).collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            state = state.copy(loading = true)
                        }
                        is Response.Failure -> {
                            state = state.copy(error = "Something went wrong.", loading = false)
                        }
                        is Response.Success -> {
                            user.email?.let { user ->
                                userRepository.getByEmail(user).collect { inner ->
                                    when (inner) {
                                        is Response.Success -> {
                                            inner.data?.let {
                                                it.avatar = response.data.toString()
                                                userRepository.save(it).collect { final ->
                                                    state = when (final) {
                                                        is Response.Success -> {
                                                            state.copy(success = true, loading = false)
                                                        }

                                                        is Response.Failure -> {
                                                            state.copy(error = "Something went wrong.", loading = false)
                                                        }

                                                        is Response.Loading -> {
                                                            state.copy(loading = true)
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        is Response.Failure -> {
                                            state = state.copy(error = "Something went wrong.", loading = false)
                                        }

                                        is Response.Loading -> {
                                            state = state.copy(loading = true)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}