package com.excercise.weatherapp.core.data

import com.excercise.weatherapp.core.data.source.local.LocalDataSource
import com.excercise.weatherapp.core.data.source.remote.RemoteDataSource
import com.excercise.weatherapp.core.domain.repository.IWeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IWeatherRepository {
}