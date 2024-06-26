package com.elfak.tempest.presentation.home.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.elfak.tempest.common.view_models.AuthViewModel
import com.elfak.tempest.model.Ticket
import com.elfak.tempest.model.User
import com.elfak.tempest.utility.navigation.Screen
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
fun Map(
    properties: MapProperties,
    camera: CameraPositionState,
    currentLocation: Pair<Double, Double>?,
    navController: NavController,
    users: List<User>,
    tickets: List<Ticket>,
) {
    val authViewModel = AuthViewModel()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = properties,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            mapToolbarEnabled = false,
        ),
        cameraPositionState = camera
    ) {
        currentLocation?.let { location ->
            Dot(
                position = LatLng(location.first, location.second),
                color = Color(0xFF54D490)
            ) {
                val user = authViewModel.current
                user?.let {
                    navController.navigate(Screen.UserPreview.createRoute(it.id))
                }
            }
        }
        users.forEach { user ->
            Dot(
                position = LatLng(user.latitude, user.longitude),
                color = Color(0xFF266DF0)
            ) {
                navController.navigate(Screen.UserPreview.createRoute(user.id))
            }
        }
        tickets.forEach { ticket ->
            val color: Color = when (ticket.priority) {
                "Low" -> {
                    Color(0xFF75777C)
                }

                "Medium" -> {
                    Color(0xFFEDD308)
                }

                "High" -> {
                    Color(0xFFFF5B59)
                }

                else -> {
                    Color(0xFF75777C)
                }
            }

            if (!ticket.solved) {
                Dot(
                    position = LatLng(
                        ticket.latitude,
                        ticket.longitude
                    ),
                    color = color
                ) {
                    navController.navigate(Screen.TicketPreview.createRoute(ticket.id))
                }
            }
        }
    }
}