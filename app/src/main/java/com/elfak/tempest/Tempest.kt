package com.elfak.tempest

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.elfak.tempest.presentation.shared.preferences.AuthPreferences
import com.elfak.tempest.presentation.shared.preferences.AvatarPreferences
import com.google.firebase.FirebaseApp

class Tempest: Application() {
    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel("location", "Location", NotificationManager.IMPORTANCE_LOW)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        AuthPreferences.init(this)
        AvatarPreferences.init(this)
        FirebaseApp.initializeApp(this)
    }
}