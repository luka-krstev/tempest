package com.elfak.tempest.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elfak.tempest.navigation.Screen
import com.elfak.tempest.presentation.shared.view_models.AuthViewModel
import com.google.android.gms.maps.UiSettings
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings

@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel = viewModel<HomeViewModel>()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = homeViewModel.state.properties,
        uiSettings = MapUiSettings(zoomControlsEnabled = false)
    )
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController()
    )
}