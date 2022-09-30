package com.rijaldev.history.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun String.toAnotherDate(): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val date = df.parse(this) as Date
        return DateFormat.getDateInstance(DateFormat.FULL).format(date)
    }
}