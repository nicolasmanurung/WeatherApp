package com.excercise.weatherapp.core.data.source.local

import com.excercise.weatherapp.core.data.source.local.entity.DailyWeatherEntity
import com.excercise.weatherapp.core.data.source.local.entity.WeatherEntity
import com.excercise.weatherapp.core.data.source.local.room.WeatherDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val weatherDao: WeatherDao) {
    // GET
    fun getDetailWeatherDataByNameCountry(
        nameCity: String
    ): Flow<List<WeatherEntity>> {
        return weatherDao.getDetailWeatherByName(nameCity)
    }

    fun getDetailWeatherDataByIdCountry(
        idCountry: Int
    ): Flow<List<WeatherEntity>> {
        return weatherDao.getDetailWeatherById(idCountry)
    }

    fun getWeeklyWeatherDataById(
        idCountry: Int
    ): Flow<List<DailyWeatherEntity>> {
        return weatherDao.getWeeklyWeatherByIdCountry(idCountry)
    }

    fun getWeeklyWeatherDataByCoordinate(
        lat: Double,
        lon: Double
    ): Flow<List<DailyWeatherEntity>> {
        return weatherDao.getWeeklyWeatherByCoordinate(lat, lon)
    }

    fun getLatestCacheCurrentWeather() : Flow<List<WeatherEntity>> = weatherDao.getDetailWeatherCache()

    fun getFavoriteCountry() = weatherDao.getAllFavoriteCountry()

    // INSERT
    suspend fun insertDetailWeather(detailWeather: WeatherEntity) =
        weatherDao.insertDetailWeather(detailWeather)

    suspend fun insertDailyEstimatedWeather(
        idCountry: Int,
        listDailyData: List<DailyWeatherEntity>
    ) = weatherDao.updateDailyDataFromServer(idCountry, listDailyData)
}