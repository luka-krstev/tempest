package com.elfak.tempest.utility.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>
    fun getActiveUserLocations(): Flow<List<UserLocation>>

    class LocationException(message: String): Exception()
}