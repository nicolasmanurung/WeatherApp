package com.excercise.weatherapp.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val dt: Int,
    val clouds: Int,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val wind_speed: Double,
    val lat: Double,
    val lon: Double,
    val iconUrl: String,
    val weatherName: String,
    val weatherDescription: String
)

@Entity(tableName = "daily")
data class DailyWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var pkDailyWeather: Long,
    var weatherFK: Int,
    val dt: Int,
    val clouds: Int,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    val wind_speed: Double,
    val lat: Double,
    val lon: Double,
    val iconUrl: String,
    val weatherName: String,
    val weatherDescription: String,
)

@Entity(tableName = "favoritecountry")
data class FavoriteCountryByCoordinate(
    @PrimaryKey
    var pkFavoriteCountery: Long,
    var nameCountry: String,
    var lat: Double,
    var lon: Double
)