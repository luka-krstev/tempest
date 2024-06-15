package com.elfak.tempest.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.Manifest
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elfak.tempest.isServiceRunning
import com.elfak.tempest.location.NativeLocationClient
import com.elfak.tempest.location.UserLocation
import com.elfak.tempest.services.LocationService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel = viewModel<HomeViewModel>()
    val context = LocalContext.current as ComponentActivity
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
        )
    )
    var activeUsersLocations by rememberSaveable { mutableStateOf<List<UserLocation>>(emptyList()) }
    val cameraPositionState = rememberCameraPositionState()
    var currentLocation by rememberSaveable { mutableStateOf<Pair<Double, Double>?>(null) }
    var active by rememberSaveable { mutableStateOf(context.isServiceRunning(LocationService::class.java)) }
    val locationClient = NativeLocationClient(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            locationClient.getLocationUpdates(1000L)
                .catch { exception -> exception.printStackTrace() }
                .onEach { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude
                    if (currentLocation == null) {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(
                            LatLng(latitude, longitude), 16f
                        )
                    }
                    currentLocation = Pair(latitude, longitude)
                }
                .launchIn(this)
        }
    }

    LaunchedEffect(Unit) {
        locationClient.getActiveUserLocations()
            .catch { exception -> exception.printStackTrace() }
            .onEach { locations ->
                activeUsersLocations = locations
            }
            .launchIn(this)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = homeViewModel.state.properties,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false,
            ),
            cameraPositionState = cameraPositionState
        ) {
            currentLocation?.let { location ->
                Dot(
                    title = "Current user",
                    position = LatLng(location.first, location.second),
                    color = Color(0xFF54D490)
                )
            }
            activeUsersLocations.forEach { userLocation ->
                Dot(
                    position = LatLng(userLocation.latitude, userLocation.longitude),
                    title = "User: ${userLocation.id}",
                    color = Color(0xFF266DF0)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(
                24.dp,
                32.dp,
            ),
            horizontalAlignment = Alignment.End
        ) {
            Spacer(modifier = Modifier.weight(1f))
            RoundedButton(
                active = active
            ) {
                if (!permissionsState.allPermissionsGranted) {
                    permissionsState.launchMultiplePermissionRequest()
                }

                if (active) {
                    Intent(context, LocationService::class.java).apply {
                        action = LocationService.ACTION_STOP
                        context.startService(this)
                    }
                } else {
                    Intent(context, LocationService::class.java).apply {
                        action = LocationService.ACTION_START
                        context.startService(this)
                    }
                }

                active = !active
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController()
    )
}