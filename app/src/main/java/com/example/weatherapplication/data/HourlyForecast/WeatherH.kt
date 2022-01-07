package com.example.weatherapplication.data.HourlyForecast


import com.google.gson.annotations.SerializedName

data class WeatherH(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)