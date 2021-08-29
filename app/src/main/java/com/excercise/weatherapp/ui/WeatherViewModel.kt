package com.excercise.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.excercise.weatherapp.core.domain.usecase.weather.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase
) : ViewModel() {
    fun getLatestDetailData(
        nameCountry: String?,
        lat: Double,
        lon: Double
    ) = weatherUseCase.getLatestDetailWeather(nameCountry, lat, lon).asLiveData()

    fun getLatestWeeklyData(
        idCity: Int,
        lat: Double,
        lon: Double
    ) = weatherUseCase.getListEstimateWeather(idCity, lat, lon).asLiveData()

    fun getCacheDetailData(
        idCity: Int
    ) = weatherUseCase.getCacheDetailWeather(idCity).asLiveData()

    fun getCacheListWeeklyData(
        idCity: Int
    ) = weatherUseCase.getCacheListEstimateWeather(idCity).asLiveData()

    fun getFavoriteCountry() = weatherUseCase.getFavoriteCountry()
}