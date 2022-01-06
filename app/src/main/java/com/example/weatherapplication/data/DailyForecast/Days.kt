package com.example.weatherapplication.data.DailyForecast


import com.google.gson.annotations.SerializedName

data class Days(
    val clouds: Int,
    val deg: Int,
    val dt: Long,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike,
    val gust: Double,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val speed: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val weather: List<Weather>
)