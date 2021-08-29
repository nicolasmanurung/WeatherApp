package com.excercise.weatherapp.core.domain.usecase.weather

import com.excercise.weatherapp.core.domain.repository.IWeatherRepository
import javax.inject.Inject

class WeatherInteractor @Inject constructor(private val repository: IWeatherRepository) :
    WeatherUseCase {

}