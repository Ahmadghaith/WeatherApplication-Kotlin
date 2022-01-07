package com.example.weatherapplication.data.TodayForecast


data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)