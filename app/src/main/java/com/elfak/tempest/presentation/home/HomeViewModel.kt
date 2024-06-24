package com.elfak.tempest.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfak.tempest.utility.location.NativeLocationClient
import com.elfak.tempest.model.Ticket
import com.elfak.tempest.model.User
import com.elfak.tempest.presentation.filter.FilterState
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.repository.TicketRepository
import com.elfak.tempest.repository.UserRepository
import com.elfak.tempest.utility.Response
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class HomeViewModel(
    private val locationClient: NativeLocationClient,
    serviceActive: Boolean
) : ViewModel() {
    private val ticketRepository = TicketRepository()
    private val userRepository = UserRepository()
    private val authRepository = AuthRepository()

    var filter = FilterState()

    var maps: MapState by mutableStateOf(MapState())
        private set

    var service by mutableStateOf(serviceActive)
        private set

    var users by mutableStateOf<List<User>>(emptyList())
        private set

    var tickets by mutableStateOf<List<Ticket>>(emptyList())
        private set

    var current by mutableStateOf<Pair<Double, Double>?>(null)
        private set

    var camera: CameraPositionState by mutableStateOf(CameraPositionState())
        private set

    init {
        startLocationUpdates()
        loadTickets()
        loadUsers()
    }

    fun filter(filter: FilterState) {
        this.filter = filter
        loadTickets()
    }

    private fun loadTickets() {
        ticketRepository.get(filter, current)
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

    private fun loadUsers() {
        userRepository.get()
            .catch { exception -> exception.printStackTrace() }
            .map { response ->
                when(response) {
                    is Response.Success -> {
                        val current = authRepository.current()
                        current?.let {
                            it.email?.let { email ->
                                val filtered = response.data.filter { user ->
                                    user.email != email && user.service
                                }
                                Response.Success(filtered)
                            }
                        }
                    }
                    else -> response
                }
            }
            .onEach { response ->
                when(response) {
                    is Response.Success -> {
                        users = response.data
                    }
                    else -> { }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun startLocationUpdates() {
        locationClient.getLocationUpdates(1000L)
            .catch { exception -> exception.printStackTrace() }
            .onEach { location ->
                if (camera.position.target.latitude == 0.0 && camera.position.target.longitude == 0.0) {
                    camera.position = CameraPosition.fromLatLngZoom(
                        LatLng(location.latitude, location.longitude), 16f
                    )
                }
                current = Pair(location.latitude, location.longitude)
                loadTickets()
            }
            .launchIn(viewModelScope)
    }

    fun toggleService(
        active: () -> Unit,
        inactive: () -> Unit,
    ) {
        if (service) {
            active()
        } else {
            inactive()
        }
        service = !service
    }
}