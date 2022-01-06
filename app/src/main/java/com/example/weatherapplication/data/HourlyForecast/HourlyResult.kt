package com.example.weatherapplication.data.HourlyForecast


import com.google.gson.annotations.SerializedName

data class HourlyResult(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<>,
    val message: Int
)