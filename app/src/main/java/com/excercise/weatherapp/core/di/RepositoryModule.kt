package com.excercise.weatherapp.core.di

import com.excercise.weatherapp.core.data.WeatherRepository
import com.excercise.weatherapp.core.domain.repository.IWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun prodivdeRepository(weatherRepository: WeatherRepository): IWeatherRepository
}