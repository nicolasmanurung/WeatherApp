package com.excercise.weatherapp.core.domain.usecase.weather

import androidx.lifecycle.LiveData
import com.excercise.weatherapp.core.data.Resource
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.core.domain.model.MDailyWeather
import com.excercise.weatherapp.core.domain.model.MWeather
import kotlinx.coroutines.flow.Flow

interface WeatherUseCase {
    fun getLatestDetailWeather(
        nameCountry: String?,
        lat: Double,
        lon: Double
    ): Flow<Resource<List<MWeather>>>

    fun getCacheDetailWeather(
        idCity: Int
    ): Flow<Resource<List<MWeather>>>

    fun getListEstimateWeather(
        idCity: Int,
        lat: Double,
        lon: Double
    ): Flow<Resource<MDailyWeather>>

    fun getCacheListEstimateWeather(
        idCity: Int
    ): Flow<Resource<MDailyWeather>>

    fun getFavoriteCountry(): LiveData<List<FavoriteCountryByCoordinate>>
}