package com.elfak.tempest.presentation.shared.preferences

import android.content.Context
import android.content.SharedPreferences

object AuthPreferences {
    private lateinit var sharedPreferences: SharedPreferences
    private const val PREFERENCES_NAME = "auth_preferences"
    private const val KEY = "user_id"

    fun init (context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun getUserID(): String? {
        return sharedPreferences.getString(KEY, null)
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

    fun setUserID(userId: String?) {
        sharedPreferences.edit().putString(KEY, userId).apply()
    }
}