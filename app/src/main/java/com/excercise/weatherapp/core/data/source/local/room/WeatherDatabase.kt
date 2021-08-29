package com.excercise.weatherapp.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.excercise.weatherapp.core.data.source.local.entity.DailyWeatherEntity
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.core.data.source.local.entity.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        DailyWeatherEntity::class,
        FavoriteCountryByCoordinate::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}