package com.elfak.tempest.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val avatar: String = "",
    val email: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val fullName: String = "",
    val phone: String = "",
    val points: Int = 0,
    val service: Boolean = false,
    val timestamp: Long = 0,
    val username: String = ""
) : Parcelable