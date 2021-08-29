package com.excercise.weatherapp.core.data.source.remote.network

import com.excercise.weatherapp.core.data.source.remote.response.DetailWeatherResponse
import com.excercise.weatherapp.core.data.source.remote.response.ListWeatherWeeklyResponse
import com.excercise.weatherapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getDetailWeatherByCountryName(
        @Query("q") q: String?,
        @Query("lat") lat: Double?,
        @Query("lon") lot: Double?,
        @Query("appid") appid: String = Constants.API_KEY,
        @Query("units") units: String = Constants.TYPE_TEMP
    ): Response<DetailWeatherResponse>

    @GET("data/2.5/onecall")
    suspend fun getEstimatedWeeklyWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = Constants.API_KEY,
        @Query("exclude") exclude: String = "minutely,hourly",
        @Query("units") units: String = Constants.TYPE_TEMP
    ): Response<ListWeatherWeeklyResponse>
}