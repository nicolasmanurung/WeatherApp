package com.excercise.weatherapp.core.data.source.remote

import android.util.Log
import com.excercise.weatherapp.core.data.source.remote.network.ApiResponse
import com.excercise.weatherapp.core.data.source.remote.network.WeatherApiService
import com.excercise.weatherapp.core.data.source.remote.response.DetailWeatherResponse
import com.excercise.weatherapp.core.data.source.remote.response.ListWeatherWeeklyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: WeatherApiService) {
    suspend fun getCurrentDetailWeather(
        countryName: String?,
        lot: Double?,
        lat: Double?
    ): Flow<ApiResponse<DetailWeatherResponse>> {
        return flow {
            try {
                val response =
                    apiService.getDetailWeatherByCountryName(q = countryName, lot = lot, lat = lat)
                Log.d("RESPONSE->", response.message().toString())
                if (response.isSuccessful) {
                    val dataResponse = response.body()
                    if (dataResponse != null) {
                        emit(ApiResponse.Success(dataResponse))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                } else {
                    emit(ApiResponse.Error(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getEstimatedWeeklyWeather(
        lat: Double,
        lon: Double
    ): Flow<ApiResponse<ListWeatherWeeklyResponse>> {
        return flow {
            try {
                val response = apiService.getEstimatedWeeklyWeather(lat, lon)
                if (response.isSuccessful) {
                    val dataArray = response.body()?.daily
                    Log.d("DATAARRAY->", dataArray.toString())
                    if (dataArray?.isNotEmpty() == true) {
                        emit(ApiResponse.Success(response.body()!!))
                    } else {
                        emit(ApiResponse.Empty)
                    }
                } else {
                    emit(ApiResponse.Error(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}