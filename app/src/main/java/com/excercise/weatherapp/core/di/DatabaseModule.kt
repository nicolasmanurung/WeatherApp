package com.excercise.weatherapp.core.di

import android.content.Context
import androidx.room.Room
import com.excercise.weatherapp.core.data.source.local.room.WeatherDao
import com.excercise.weatherapp.core.data.source.local.room.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase =
        Room.databaseBuilder(
            context,
            WeatherDatabase::class.java, "weather.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideWeatherDao(database: WeatherDatabase): WeatherDao = database.weatherDao()
}