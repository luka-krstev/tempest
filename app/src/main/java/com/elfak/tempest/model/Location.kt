package com.elfak.tempest.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val id: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
): Parcelable
