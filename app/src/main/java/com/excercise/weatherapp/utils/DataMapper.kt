package com.excercise.weatherapp.utils

import com.excercise.weatherapp.core.data.source.local.entity.DailyWeatherEntity
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.core.data.source.local.entity.WeatherEntity
import com.excercise.weatherapp.core.data.source.remote.response.DetailWeatherResponse
import com.excercise.weatherapp.core.data.source.remote.response.ListWeatherWeeklyResponse
import com.excercise.weatherapp.core.domain.model.DailyWeather
import com.excercise.weatherapp.core.domain.model.MDailyWeather
import com.excercise.weatherapp.core.domain.model.MWeather

object DataMapper {
    val listFavoriteLocation = arrayListOf<FavoriteCountryByCoordinate>(
        FavoriteCountryByCoordinate(
            0,
            "Gdansk",
            18.6464,
            54.3521
        ),
        FavoriteCountryByCoordinate(
            1,
            "Warszawa",
            52.2298,
            21.0118
        ),
        FavoriteCountryByCoordinate(
            2,
            "Krakow",
            50.0833,
            19.9167
        ),
        FavoriteCountryByCoordinate(
            3,
            "Wroclaw",
            51.1,
            17.0333
        ),
        FavoriteCountryByCoordinate(
            4,
            "Lodz",
            51.75,
            19.4667
        )
    )

    fun mapCurrentDetailWeatherResponseToEntity(input: DetailWeatherResponse): WeatherEntity {
        return WeatherEntity(
            id = input.id,
            name = input.name.removeNonSpacingMarks(),
            dt = input.dt,
            clouds = input.clouds.all,
            humidity = input.main.humidity,
            pressure = input.main.pressure,
            temp = input.main.temp,
            wind_speed = input.wind.speed,
            lat = input.coord.lat,
            lon = input.coord.lon,
            iconUrl = input.weather.first().icon,
            weatherName = input.weather.first().main,
            weatherDescription = input.weather.first().description
        )
    }

    fun mapWeatherDetailEntityToDomain(input: List<WeatherEntity>): List<MWeather> {
        return input.map {
            MWeather(
                id = it.id,
                name = it.name.removeNonSpacingMarks(),
                dt = it.dt,
                clouds = it.clouds,
                humidity = it.humidity,
                pressure = it.pressure,
                temp = it.temp,
                wind_speed = it.wind_speed,
                lat = it.lat,
                lon = it.lon,
                iconUrl = it.iconUrl,
                weatherName = it.weatherName,
                weatherDescription = it.weatherDescription
            )
        }
    }

    fun mapWeatherWeeklyResponseToEntities(
        idCountry: Int,
        input: ListWeatherWeeklyResponse
    ): ArrayList<DailyWeatherEntity> {
        val listDailyEntity = ArrayList<DailyWeatherEntity>()
        input.daily.forEach { item ->
            val newDaily = DailyWeatherEntity(
                pkDailyWeather = 0,
                weatherFK = idCountry,
                dt = item.dt,
                clouds = item.clouds,
                humidity = item.humidity,
                pressure = item.pressure,
                temp = item.temp.day,
                wind_speed = item.wind_speed,
                lat = input.lat,
                lon = input.lon,
                iconUrl = item.weather.first().icon,
                weatherName = item.weather.first().main,
                weatherDescription = item.weather.first().description
            )
            listDailyEntity.add(newDaily)
        }
        return listDailyEntity
    }

    fun mapDailyWeatherEntityToDomain(input: List<DailyWeatherEntity>): MDailyWeather {
        val listDailyModel = ArrayList<DailyWeather>()
        input.forEach { item ->
            val newDaily = DailyWeather(
                dt = item.dt,
                clouds = item.clouds,
                humidity = item.humidity,
                pressure = item.pressure,
                temp = item.temp,
                wind_speed = item.wind_speed,
                lat = item.lat,
                lon = item.lon,
                iconUrl = item.iconUrl,
                weatherName = item.weatherName,
                weatherDescription = item.weatherDescription
            )
            listDailyModel.add(newDaily)
        }

        return MDailyWeather(
            listData = listDailyModel
        )
    }
}