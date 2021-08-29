package com.excercise.weatherapp.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MWeather(
    val id: Int,
    val name: String,
    val dt: Int,
    val clouds: Int,
    val humidity: Int?,
    val pressure: Int,
    val temp: Double,
    val wind_speed: Double,
    val lat: Double,
    val lon: Double,
    val iconUrl: String,
    val weatherName: String,
    val weatherDescription: String
) : Parcelable

@Parcelize
data class MDailyWeather(
    val listData: List<DailyWeather>
) : Parcelable

@Parcelize
data class DailyWeather(
    val dt: Int,
    val clouds: Int,
    val humidity: Int?,
    val pressure: Int,
    val temp: Double,
    val wind_speed: Double,
    val lat: Double,
    val lon: Double,
    val iconUrl: String,
    val weatherName: String,
    val weatherDescription: String
) : Parcelable