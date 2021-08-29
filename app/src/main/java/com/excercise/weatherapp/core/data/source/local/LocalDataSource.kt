package com.excercise.weatherapp.core.data.source.local

import com.excercise.weatherapp.core.data.source.local.room.WeatherDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val weatherDao: WeatherDao) {

}