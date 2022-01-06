package com.example.weatherapplication.data.HourlyForecast


import com.google.gson.annotations.SerializedName

data class Hours(
    val clouds: Clouds,
    val dt: Long,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val main: Main,
    val pop: Int,
    val sys: Sys,
    val visibility: Int,
    val weather: List<WeatherH>,
    val wind: Wind
)