package com.excercise.weatherapp.utils

import android.content.Context
import android.util.Log
import dagger.hilt.android.internal.managers.ViewComponentManager
import okhttp3.internal.format
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*

fun String.removeNonSpacingMarks() =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")

fun Int.toDate(
    parserFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    dateFormat: String = "yyyy M d"
): String {
    val parser = SimpleDateFormat(parserFormat, Locale.ENGLISH)
    val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    val toInstants = Instant.ofEpochMilli(this.times(1000L))
    Log.d("PARSE->", toInstants.toString())
    return formatter.format(parser.parse(toInstants.toString()))
}

fun Context.activityContext(): Context? {
    return if (this is ViewComponentManager.FragmentContextWrapper) {
        this.baseContext
    } else this
}
