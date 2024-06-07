package com.elfak.tempest.presentation.shared.preferences

import android.content.Context
import android.content.SharedPreferences

object AvatarPreferences {
    private lateinit var sharedPreferences: SharedPreferences
    private const val PREFERENCES_NAME = "avatar_preferences"
    private const val KEY = "exists"

    fun init (context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun isEmpty(): Boolean {
        return sharedPreferences.all.isEmpty()
    }

    fun getExists(): Boolean {
        return sharedPreferences.getBoolean(KEY, true)
    }

    fun setExists(state: Boolean) {
        sharedPreferences.edit().putBoolean(KEY, state).apply()
    }
}