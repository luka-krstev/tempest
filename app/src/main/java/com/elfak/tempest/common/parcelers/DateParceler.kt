package com.elfak.tempest.common.parcelers

import android.os.Parcel
import kotlinx.parcelize.Parceler
import java.util.Date

object DateParceler : Parceler<Date> {
    override fun create(parcel: Parcel): Date {
        return Date(parcel.readLong())
    }

    override fun Date.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(time)
    }
}