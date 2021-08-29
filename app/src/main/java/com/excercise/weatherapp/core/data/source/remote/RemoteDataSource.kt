package com.excercise.weatherapp.core.data.source.remote

import com.excercise.weatherapp.core.data.source.remote.network.WeatherApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: WeatherApiService) {

}