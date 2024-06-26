package com.elfak.tempest.utility.location

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*


fun calculateBoundingBox(center: Pair<Double, Double>?, radius: Double): Pair<LatLng, LatLng> {
    val earth = 6371.0 * 1000

    val lat = center!!.first
    val lon = center.second

    val deltaLat = radius / earth
    val deltaLon = radius / (earth * cos(Math.toRadians(lat)))

    val minLat = lat - Math.toDegrees(deltaLat)
    val maxLat = lat + Math.toDegrees(deltaLat)
    val minLon = lon - Math.toDegrees(deltaLon)
    val maxLon = lon + Math.toDegrees(deltaLon)

    return Pair(LatLng(minLat, minLon), LatLng(maxLat, maxLon))
}