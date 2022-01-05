package com.example.weatherapplication.data.DailyForecast


import com.google.gson.annotations.SerializedName

data class DailyForecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Days>,
    val message: Double
)