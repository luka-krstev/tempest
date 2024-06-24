package com.elfak.tempest.presentation.home

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elfak.tempest.R
import com.elfak.tempest.common.view_models.AuthViewModel
import com.elfak.tempest.isServiceRunning
import com.elfak.tempest.presentation.filter.FilterViewModel
import com.elfak.tempest.utility.location.NativeLocationClient
import com.elfak.tempest.utility.navigation.Screen
import com.elfak.tempest.presentation.home.components.Map
import com.elfak.tempest.presentation.home.components.Option
import com.elfak.tempest.presentation.home.components.Pill
import com.elfak.tempest.presentation.home.components.RoundedButton
import com.elfak.tempest.services.LocationService
import com.google.android.gms.location.LocationServices

@Composable
fun HomeScreen(navController: NavController, filterViewModel: FilterViewModel) {
    val context = LocalContext.current as ComponentActivity
    val locationClient = NativeLocationClient(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )

    val factory = remember {
        HomeViewModelFactory(
            locationClient,
            context.isServiceRunning(LocationService::class.java)
        )
    }
    val authViewModel = viewModel<AuthViewModel>()
    val homeViewModel = viewModel<HomeViewModel>(factory = factory)

    LaunchedEffect(filterViewModel.state) {
        homeViewModel.filter(filterViewModel.state)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Map(
                properties = homeViewModel.maps.properties,
                camera = homeViewModel.camera,
                currentLocation = homeViewModel.current,
                navController = navController,
                tickets = homeViewModel.tickets,
                users = homeViewModel.users
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(0.dp, 16.dp, 0.dp, 0.dp)
            ) {
                Pill(text = "Tickets") {
                    navController.navigate(
                        Screen.Tickets.createRoute(homeViewModel.current?.first, homeViewModel.current?.second)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Pill(text = "Users") {
                    navController.navigate(Screen.Users.route)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Option(
                    description = "Exit",
                    icon = R.drawable.exit,
                ) {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        24.dp,
                        32.dp,
                    ),
                horizontalAlignment = Alignment.End
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Option(
                        description = "Filter tickets",
                        icon = R.drawable.filter,
                    ) {
                        navController.navigate(Screen.Filters.route)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Option(
                        description = "Create a new ticket",
                        icon = R.drawable.note,
                    ) {
                        homeViewModel.current?.let {
                            navController.navigate(
                                Screen.ModifyTicket.createRoute(
                                    it.first,
                                    it.second
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    RoundedButton(
                        active = homeViewModel.service
                    ) {
                        homeViewModel.toggleService(
                            active = {
                                Intent(context, LocationService::class.java).apply {
                                    action = LocationService.ACTION_STOP
                                    context.startService(this)
                                }
                            },
                            inactive = {
                                Intent(context, LocationService::class.java).apply {
                                    action = LocationService.ACTION_START
                                    context.startService(this)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}