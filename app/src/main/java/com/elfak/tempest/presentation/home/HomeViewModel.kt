package com.elfak.tempest.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.MapProperties

class HomeViewModel : ViewModel() {
    var state: MapState by mutableStateOf(MapState())
        private set

    data class MapState (
        val properties: MapProperties = MapProperties(
            mapStyleOptions = MapStyleOptions(MapStyle.style)
        )
    )


}