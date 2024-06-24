package com.elfak.tempest.services

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.elfak.tempest.R
import com.elfak.tempest.model.Ticket
import com.elfak.tempest.model.User
import com.elfak.tempest.presentation.filter.FilterState
import com.elfak.tempest.repository.AuthRepository
import com.elfak.tempest.repository.TicketRepository
import com.elfak.tempest.repository.UserRepository
import com.elfak.tempest.utility.Response
import com.elfak.tempest.utility.location.LocationClient
import com.elfak.tempest.utility.location.NativeLocationClient
import com.google.android.gms.location.LocationServices
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
    private val ticketRepository = TicketRepository()
    private val userRepository = UserRepository()
    private val authRepository = AuthRepository()
    private var notified: List<Ticket> = emptyList()
    private var user: User? = null

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_stat)
            .setOngoing(true)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(1000L)
            .catch { exception -> exception.printStackTrace() }
            .onEach { location ->
                val updated = notification.setContentText(
                    "Location: (${location.latitude}, ${location.longitude}"
                )

                user?.let {
                    updateUser(
                        location.latitude,
                        location.longitude,
                        true
                    )

                    fetchTickets(location.latitude, location.longitude)
                }

                manager.notify(1, updated.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        updateUser(0.0, 0.0, false)
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    private fun notifyUser(ticket: Ticket) {
        val builder = NotificationCompat.Builder(this, "tickets")
            .setSmallIcon(R.drawable.ic_stat)
            .setContentTitle(ticket.title)
            .setContentText(ticket.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(ticket.id.hashCode(), builder.build())
        }
    }

    private fun fetchTickets(latitude: Double, longitude: Double) {
        var filter = FilterState()
        filter = filter.copy(radius = 1000)

        serviceScope.launch {
            ticketRepository
                .get(filter, Pair(latitude, longitude))
                .collect { response ->
                    when (response) {
                        is Response.Success -> {
                            response.data.forEach { ticket ->
                                if (!notified.contains(ticket)) {
                                    notified += ticket
                                    notifyUser(ticket)
                                }
                            }
                        }
                        is Response.Failure -> {
                            response.message
                        }
                        else -> { }
                    }
                }
        }
    }

    private fun updateUser(latitude: Double, longitude: Double, service: Boolean) {
        user?.let {
            user = it.copy(
                latitude = latitude,
                longitude = longitude,
                service = service
            )

            serviceScope.launch {
                userRepository
                    .save(it)
                    .collect { response ->
                        when (response) {
                            is Response.Failure -> {
                                response.message.printStackTrace()
                            }

                            else -> {}
                        }
                    }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = NativeLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

        val current = authRepository.current()
        current?.let {
            it.email?.let { email ->
                serviceScope.launch {
                    userRepository
                        .getByEmail(email)
                        .collect { response ->
                            when (response) {
                                is Response.Success -> {
                                    response.data?.let { data ->
                                        user = data
                                    }
                                }
                                is Response.Failure -> {
                                    response.message
                                }
                                else -> { }
                            }
                        }
                }
            }
        }
    }

    override fun onDestroy() {
        updateUser(0.0, 0.0, false)
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): Binder? {
        return null
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}