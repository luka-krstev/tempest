package com.elfak.tempest.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.elfak.tempest.hasLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class NativeLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
): LocationClient {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var userLocationsListener: ListenerRegistration? = null

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if(!context.hasLocationPermission()) {
                throw LocationClient.LocationException("Missing location permission")
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                throw LocationClient.LocationException("GPS is disabled")
            }

            val request = LocationRequest
                .Builder(interval)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(request, callback, Looper.getMainLooper())

            awaitClose {
                client.removeLocationUpdates(callback)
            }
        }
    }

    override fun getActiveUserLocations(): Flow<List<UserLocation>> = callbackFlow {
        userLocationsListener = firestore.collection("users")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    exception.printStackTrace()
                    return@addSnapshotListener
                }
                snapshot?.let {
                    val locations = it.documents.mapNotNull { doc ->
                        val latitude = doc.getDouble("latitude")
                        val longitude = doc.getDouble("longitude")
                        val active = doc.getBoolean("serviceActive")
                        val email = doc.getString("email")
                        if (latitude != null && longitude != null && active != null && active && firebaseAuth.currentUser?.email != email) {
                            UserLocation(doc.id, latitude, longitude)
                        } else {
                            null
                        }
                    }
                    launch { send(locations) }
                }
            }

        awaitClose {
            userLocationsListener?.remove()
        }
    }
}