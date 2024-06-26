package com.elfak.tempest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.FirebaseApp

class Tempest: Application() {
    override fun onCreate() {
        super.onCreate()

        val location = NotificationChannel("location", "Location", NotificationManager.IMPORTANCE_HIGH)
        val tickets = NotificationChannel("tickets", "tickets", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(location)
        notificationManager.createNotificationChannel(tickets)
        FirebaseApp.initializeApp(this)
    }
}