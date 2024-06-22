package com.elfak.tempest.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.elfak.tempest.R
import com.elfak.tempest.model.User
import com.elfak.tempest.utility.location.LocationClient
import com.elfak.tempest.utility.location.NativeLocationClient
import com.elfak.tempest.presentation.MainActivity
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.repository.UserRepository
import com.elfak.tempest.utility.Response
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocationService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val userRepository = UserRepository()
    private val authRepository = AuthRepository()
    private lateinit var user: User

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = NativeLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        serviceScope.launch {
            authRepository.current()?.let {
                userRepository.getById(it.uid)
                    .catch { exception -> exception.printStackTrace() }
                    .collect { response ->
                        when(response) {
                            is Response.Success -> {
                                response.data?.let {
                                    user = it
                                }
                            }
                            else -> { }
                        }
                    }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startService()
            ACTION_STOP -> stopService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService() {
        val notification = createNotification()

        startForeground(FOREGROUND_SERVICE_ID, notification.build())

        locationClient.getLocationUpdates(1000L)
            .catch { exception ->
                exception.printStackTrace()
            }
            .onEach { location ->
                updateLocation(location.latitude, location.longitude)
                checkNearbyTickets(location.latitude, location.longitude)
            }
            .launchIn(serviceScope)
    }

    private fun createNotification(): NotificationCompat.Builder {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: unknown")
            .setSmallIcon(R.drawable.ic_stat)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
    }

    private fun updateLocation(latitude: Double, longitude: Double) {
        firestore.collection("users").document(user.id)
            .update(
                "latitude", latitude,
                "longitude", longitude,
                "timestamp", System.currentTimeMillis()
            )
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun checkNearbyTickets(userLatitude: Double, userLongitude: Double) {
        firestore.collection("tickets")
            .whereEqualTo("solved", false)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val ticketLatitude = document.getDouble("latitude")
                    val ticketLongitude = document.getDouble("longitude")
                    if (ticketLatitude != null && ticketLongitude != null) {
                        if (isNearby(userLatitude, userLongitude, ticketLatitude, ticketLongitude)) {
                            // Handle the nearby ticket
                            // E.g., send a notification or update UI
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun isNearby(userLatitude: Double, userLongitude: Double, ticketLatitude: Double, ticketLongitude: Double): Boolean {
        val radius = 1000 // 1km radius
        val results = FloatArray(1)
        Location.distanceBetween(userLatitude, userLongitude, ticketLatitude, ticketLongitude, results)
        return results[0] < radius
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    private fun updateServiceStatus(isActive: Boolean) {
        val statusData = mapOf("serviceActive" to isActive)
        firestore.collection("users").document(user.id)
            .update(statusData)
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        updateServiceStatus(false)
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val FOREGROUND_SERVICE_ID = 1
    }
}