package com.excercise.weatherapp.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.excercise.weatherapp.core.data.source.local.entity.DailyWeatherEntity
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.core.data.source.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    // GET
    @Query("SELECT * FROM weather WHERE id = :idCountry LIMIT 1")
    fun getDetailWeatherById(
        idCountry: Int
    ): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather WHERE name = :nameCity LIMIT 1")
    fun getDetailWeatherByName(
        nameCity: String
    ): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather LIMIT 1")
    fun getDetailWeatherCache(): Flow<List<WeatherEntity>>


    @Query("SELECT * FROM daily WHERE weatherFK = :idCountry ORDER BY dt LIMIT 7")
    fun getWeeklyWeatherByIdCountry(
        idCountry: Int
    ): Flow<List<DailyWeatherEntity>>

    @Query("SELECT * FROM daily WHERE lat = :lat AND lon =:lon ORDER BY dt LIMIT 7")
    fun getWeeklyWeatherByCoordinate(
        lat: Double,
        lon: Double
    ): Flow<List<DailyWeatherEntity>>

    @Query("SELECT * FROM favoritecountry")
    fun getAllFavoriteCountry(): LiveData<List<FavoriteCountryByCoordinate>>

    // INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetailWeather(weatherDetail: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListDailyWeather(weatherDailyList: List<DailyWeatherEntity>)

    //DELETE
    @Query("DELETE FROM daily WHERE weatherFK = :idCountry")
    suspend fun deleteDailyData(idCountry: Int)


    //Transaction
    @Transaction
    suspend fun updateDailyDataFromServer(idCountry: Int, dailyList: List<DailyWeatherEntity>) {
        deleteDailyData(idCountry)
        insertListDailyWeather(dailyList)
    }

}