package com.elfak.tempest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.google.firebase.FirebaseApp

class Tempest: Application() {
    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel("location", "Location", NotificationManager.IMPORTANCE_LOW)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        FirebaseApp.initializeApp(this)
    }
}