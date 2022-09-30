package com.rijaldev.history.utils

import android.content.Context
import android.location.Geocoder
import java.io.IOException
import java.util.*

object MapsUtil {
    fun getAddressName(lat: Double, lon: Double, context: Context): String? {
        var addressName: String? = null
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }
}