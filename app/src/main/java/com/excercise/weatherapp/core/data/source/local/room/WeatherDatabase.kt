package com.excercise.weatherapp.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [

    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}