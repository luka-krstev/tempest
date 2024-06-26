package com.elfak.tempest.presentation.filter

import android.os.Parcelable
import com.elfak.tempest.common.parcelers.DateParceler
import com.elfak.tempest.model.User
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import java.util.Date

@Parcelize
@TypeParceler<Date, DateParceler>
data class FilterState(
    val title: String = "",
    val content: String = "",
    val author: User = User(),
    val priority: String = "",
    val radius: Int? = null,
    val created: Date? = null
) : Parcelable
