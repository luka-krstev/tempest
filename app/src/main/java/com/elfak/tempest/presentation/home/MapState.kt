package com.elfak.tempest.presentation.home

import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties

data class MapState (
    val properties: MapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions(MapStyle.style)
    )
)
