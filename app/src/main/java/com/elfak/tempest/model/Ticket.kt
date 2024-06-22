package com.elfak.tempest.model

import android.os.Parcelable
import com.elfak.tempest.common.parcelers.DateParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.util.Date

@Parcelize
@TypeParceler<Date, DateParceler>
data class Ticket(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val priority: String = "",
    val timeCreated: Date = Date(),
    val userEmail: String = "",
    val solved: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
): Parcelable
