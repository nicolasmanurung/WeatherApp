package com.excercise.weatherapp.core.domain.usecase.weather

import androidx.lifecycle.LiveData
import com.excercise.weatherapp.core.data.Resource
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.core.domain.model.MDailyWeather
import com.excercise.weatherapp.core.domain.model.MWeather
import com.excercise.weatherapp.core.domain.repository.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherInteractor @Inject constructor(private val repository: IWeatherRepository) :
    WeatherUseCase {
    override fun getLatestDetailWeather(
        nameCountry: String?,
        lat: Double,
        lon: Double
    ): Flow<Resource<List<MWeather>>> = repository.getLatestDetailWeather(nameCountry, lat, lon)

    override fun getCacheDetailWeather(idCity: Int): Flow<Resource<List<MWeather>>> =
        repository.getCacheDetailWeather(idCity)

    override fun getListEstimateWeather(
        idCity: Int,
        lat: Double,
        lon: Double
    ): Flow<Resource<MDailyWeather>> = repository.getListEstimateWeather(idCity, lat, lon)

    override fun getCacheListEstimateWeather(idCity: Int): Flow<Resource<MDailyWeather>> =
        repository.getCacheListEstimateWeather(idCity)

    override fun getFavoriteCountry(): LiveData<List<FavoriteCountryByCoordinate>> =
        repository.getFavoriteCountry()


}