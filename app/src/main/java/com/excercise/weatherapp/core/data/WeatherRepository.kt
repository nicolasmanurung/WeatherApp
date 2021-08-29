package com.excercise.weatherapp.core.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.excercise.weatherapp.core.data.source.local.LocalDataSource
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.core.data.source.remote.RemoteDataSource
import com.excercise.weatherapp.core.data.source.remote.network.ApiResponse
import com.excercise.weatherapp.core.data.source.remote.response.DetailWeatherResponse
import com.excercise.weatherapp.core.data.source.remote.response.ListWeatherWeeklyResponse
import com.excercise.weatherapp.core.domain.model.MDailyWeather
import com.excercise.weatherapp.core.domain.model.MWeather
import com.excercise.weatherapp.core.domain.repository.IWeatherRepository
import com.excercise.weatherapp.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IWeatherRepository {

    override fun getLatestDetailWeather(
        nameCity: String?,
        lat: Double,
        lon: Double
    ): Flow<Resource<List<MWeather>>> =
        object : NetworkBoundResource<List<MWeather>, DetailWeatherResponse>() {
            override fun loadFromDB(): Flow<List<MWeather>> {
                return localDataSource.getDetailWeatherDataByNameCountry(nameCity.toString()).map {
                    DataMapper.mapWeatherDetailEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: List<MWeather>?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<DetailWeatherResponse>> {
                return remoteDataSource.getCurrentDetailWeather(nameCity, lat, lon)
            }

            override suspend fun saveCallResult(data: DetailWeatherResponse) {
                Log.d("SAVECALL->", data.toString())
                val detailWeather = DataMapper.mapCurrentDetailWeatherResponseToEntity(data)
                localDataSource.insertDetailWeather(detailWeather)
            }
        }.asFlow()

    override fun getCacheDetailWeather(idCity: Int): Flow<Resource<List<MWeather>>> =
        object : NetworkBoundResource<List<MWeather>, MWeather>() {
            override fun loadFromDB(): Flow<List<MWeather>> {
                return localDataSource.getLatestCacheCurrentWeather().map {
                    DataMapper.mapWeatherDetailEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: List<MWeather>?): Boolean {
                return false
            }

            override suspend fun createCall(): Flow<ApiResponse<MWeather>> {
                TODO("Not yet implemented")
            }

            override suspend fun saveCallResult(data: MWeather) {
                TODO("Not yet implemented")
            }
        }.asFlow()

    override fun getListEstimateWeather(
        idCity: Int,
        lat: Double,
        lon: Double
    ): Flow<Resource<MDailyWeather>> =
        object : NetworkBoundResource<MDailyWeather, ListWeatherWeeklyResponse>() {
            override fun loadFromDB(): Flow<MDailyWeather> {
                return localDataSource.getWeeklyWeatherDataById(idCity).map {
                    DataMapper.mapDailyWeatherEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: MDailyWeather?): Boolean {
                return true
            }

            override suspend fun createCall(): Flow<ApiResponse<ListWeatherWeeklyResponse>> {
                return remoteDataSource.getEstimatedWeeklyWeather(lat, lon)
            }

            override suspend fun saveCallResult(data: ListWeatherWeeklyResponse) {
                val listDaily = DataMapper.mapWeatherWeeklyResponseToEntities(idCity, data)
                Log.d("SCRDEBUG->", listDaily.toString())
                localDataSource.insertDailyEstimatedWeather(idCity, listDaily)
            }
        }.asFlow()


    override fun getCacheListEstimateWeather(idCity: Int): Flow<Resource<MDailyWeather>> =
        object : NetworkBoundResource<MDailyWeather, MDailyWeather>() {
            override fun loadFromDB(): Flow<MDailyWeather> {
                return localDataSource.getWeeklyWeatherDataById(idCity).map {
                    DataMapper.mapDailyWeatherEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: MDailyWeather?): Boolean {
                return false
            }

            override suspend fun createCall(): Flow<ApiResponse<MDailyWeather>> {
                TODO("Not yet implemented")
            }

            override suspend fun saveCallResult(data: MDailyWeather) {
                TODO("Not yet implemented")
            }
        }.asFlow()

    override fun getFavoriteCountry(): LiveData<List<FavoriteCountryByCoordinate>> =
        localDataSource.getFavoriteCountry()
}