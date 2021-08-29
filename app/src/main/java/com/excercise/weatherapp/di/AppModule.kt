package com.excercise.weatherapp.di

import com.excercise.weatherapp.core.domain.usecase.weather.WeatherInteractor
import com.excercise.weatherapp.core.domain.usecase.weather.WeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {
    @Binds
    @ViewModelScoped
    abstract fun provideWeatherUseCase(weatherInteractor: WeatherInteractor): WeatherUseCase
}