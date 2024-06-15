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
import android.Manifest;
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.elfak.tempest.isServiceRunning
import com.elfak.tempest.services.LocationService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel = viewModel<HomeViewModel>()
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
        )
    )
    val context = LocalContext.current as ComponentActivity
    var active by rememberSaveable { mutableStateOf(!context.isServiceRunning(LocationService::class.java)) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = homeViewModel.state.properties,
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        )
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