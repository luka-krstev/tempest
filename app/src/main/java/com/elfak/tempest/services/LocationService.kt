package com.elfak.tempest.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.elfak.tempest.R
import com.elfak.tempest.location.LocationClient
import com.elfak.tempest.location.NativeLocationClient
import com.elfak.tempest.presentation.MainActivity
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var userId: String

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = NativeLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        userId = firebaseAuth.currentUser?.uid ?: "unknown_user"
        updateServiceStatus(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat
            .Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_stat)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(3000L)
            .catch { exception -> exception.printStackTrace() }
            .onEach { location ->
                val latitude = location.latitude
                val longitude = location.longitude

                val updated = notification.setContentText(
                    "Location: ($latitude, $longitude)"
                )

                notificationManager.notify(1, updated.build())

                val locationData = mapOf(
                    "latitude" to latitude,
                    "longitude" to longitude,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("users").document(userId)
                    .update(locationData)
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                    }
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    private fun updateServiceStatus(isActive: Boolean) {
        val statusData = mapOf("serviceActive" to isActive)
        firestore.collection("users").document(userId)
            .update(statusData)
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        updateServiceStatus(false)
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}